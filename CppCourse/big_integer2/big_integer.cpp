#include "big_integer.h"
#include <iostream>
#include <string>
#include <cstdio>
#include <vector>
#include <algorithm>

using namespace std;

typedef long long ll;
typedef unsigned long long ull;
const unsigned WORD_SIZE = 32, MAX = ~0, MOST_SIGNIFICANT_BIT = (1 << 31);

//--------------------------------------------------------

int most_significant_bit (unsigned x) {
	if (MOST_SIGNIFICANT_BIT & x) return 1;
	else return 0;
}

int sign (const big_array &v) {
	unsigned last = v.back();
	if (most_significant_bit(last)) return -1;
	else return 1;
}

void set_size (big_array &v, size_t size) {
	unsigned add = 0;
	if (sign(v) == -1) add = MAX;
	while (v.size() < size) v.push_back(add);
}

void normalize_size (big_array &v) {
	int x = 0;
	if (sign(v) == -1) x = 1;
	int should = 0;
	if (x == 1) should = MAX;
	for (int i = v.size() - 1; i > 0; i--) {
		if (v[i] == should) {
			if (most_significant_bit(v[i - 1]) == x) v.pop_back();
			else break;
		} else break;
	}
}

int bit(const big_array &v, size_t i) {
	if (i / WORD_SIZE >= v.size()) return sign(v) == -1;
	else return (v[i / WORD_SIZE] >> (i % WORD_SIZE)) & 1;
}

void mul2(vector<int> &a) {
	int c = 0;
	for (size_t i = 0; i < a.size(); i++) {
		c += 2 * a[i];
		a[i] = c % 10;
		c /= 10;
	}
	if (c) {
		a.push_back(1);
	}
}

void add1(vector<int> &a) {
	for (size_t i = 0; i < a.size(); i++)
		if (a[i] == 9) {
			a[i] = 0;
		} else {
			a[i]++;
			return;
		}
	a.push_back(1);
}

void inv_set_bit(big_array &v, int i, int val) {
	int in_block = i % WORD_SIZE;
	int num_block = i / WORD_SIZE;
	v.write_at(num_block) &= ~(1 << in_block);
	if (val) v.write_at(num_block) += (1 << in_block);
}

unsigned get_block(big_array &v, int i) {
	ll st = WORD_SIZE - i % WORD_SIZE ;
	unsigned m;
	if (st == WORD_SIZE) m = ~0;
	else m = ((1 << st) - 1);
	unsigned mask1 = m & v[i / WORD_SIZE];
	unsigned mask2 = 0;
	if (st != WORD_SIZE) {
		unsigned x = v[i / WORD_SIZE + 1];
		unsigned y = x & (~m);
		y >>= st;
		mask2 = y;
	}
	return (mask1 << (WORD_SIZE - st)) + mask2;
}

void set_block(big_array &v, int i, unsigned val) {
	ll st = WORD_SIZE - i % WORD_SIZE ;
	unsigned m;
	if (st == WORD_SIZE) m = ~0;
	else m = ((1 << st) - 1);
	m <<= (WORD_SIZE - st);
	unsigned mask1 = val & m;
	unsigned mask2 = val & (~m);
	mask1 >>= (WORD_SIZE - st);
	m >>= (WORD_SIZE - st);
	v.write_at(i / WORD_SIZE) &= (~m);
	v.write_at(i / WORD_SIZE) += mask1;
	if (st != WORD_SIZE) {
		v.write_at(i / WORD_SIZE + 1) &= m;
		mask2 <<= st;
		v.write_at(i / WORD_SIZE + 1) += mask2;
	}
}

//--------------------------------------------------------

big_integer::big_integer() {
	data.push_back(0);
}

big_integer::big_integer(const big_integer& bi2) 
	: data(bi2.data) 
{}

big_integer& big_integer::operator= (const big_integer &bi2) {
	big_integer tmp(bi2);
	data.swap(tmp.data);
	return *this;
} 

big_integer::big_integer(int x) {
	data.push_back(x);
}

big_integer::big_integer(string s) {
	bool negative = false;
	size_t st = 0;
	if (s[0] == '-') {
		negative = true;
		st++;
	}
	int i = 0, j = -1;
	data.push_back(0);
	while (st < s.size() && s[st] == '0') st++;
	while (st != s.size()) {
		j++;
		if (j == WORD_SIZE) {
			j = 0;
			data.push_back(0);
			i++;
		}

		size_t p = s.size() - 1;
		if ((s[p] - '0') % 2 == 1) {
			data.write_at(i) += (1 << j);
		}
		for (size_t i1 = p; ; i1--) {
			if ((s[i1] - '0') % 2 == 1 && i1 != p) {
				s[i1 + 1] += 5;
			}
			s[i1] = (s[i1] - '0') / 2 + '0';
			if (i1 == st) break;
		}

		while (st < s.size() && s[st] == '0') st++;
	}
	
	unsigned last = data.back();
	if (most_significant_bit(last) == 1) {
		data.push_back(0);
	}
	if (negative) {
		(*this) = -(*this);
	}
}

//--------------------------------------------------------

bool operator< (const big_integer& a, const big_integer& b) {
	int s1 = sign(a.data);
	int s2 = sign(b.data);
	if (s1 < s2) return true;
	if (s1 > s2) return false;
	int sza = a.data.size();
	int szb = b.data.size();
	if (sza != szb) {
		if (s1 > 0) return sza < szb;
		else return sza > szb;
	}
	for (int i = sza - 1; i >= 0; i--) {
		if (a.data[i] < b.data[i]) return 1;
		if (a.data[i] > b.data[i]) return 0;
	}
	return 0;
}

bool operator== (const big_integer& bi1, const big_integer& bi2) {
	return !(bi1 < bi2) && !(bi2 < bi1);
}

bool operator!= (const big_integer& bi1, const big_integer& bi2) {
	return !(bi1 == bi2);
}

bool operator<= (const big_integer& bi1, const big_integer& bi2) {
	return bi1 < bi2 || bi1 == bi2;
}

bool operator> (const big_integer& bi1, const big_integer& bi2) {
	return !(bi1 <= bi2);
}

bool operator>= (const big_integer& bi1, const big_integer& bi2) {
	return !(bi1 < bi2);
}

//---------------------------------------------------------

big_integer operator+ (const big_integer& a, const big_integer& b) {
	ll x = 0;
	ll base = (ll)MAX + 1;
	big_integer res; 
	int sza = a.data.size();
	int szb = b.data.size();
	int sz = max(sza, szb) + 1;
	unsigned dopa = 0, dopb = 0;
	if (sign(a.data) == -1) dopa = ~0;
	if (sign(b.data) == -1) dopb = ~0;
	set_size(res.data, sz);	
	for (int i = 0; i < sz; i++) {
		if (i < sza) x += a.data[i];
		else x += dopa;
		if (i < szb) x += b.data[i];
		else x += dopb;
		if (i != sz -1) {
			res.data.write_at(i) = (unsigned)x;
			x /= base;
		} else {
			res.data.write_at(i) = (unsigned)x;
		}
	}
	normalize_size(res.data);
	return res;
}

big_integer& big_integer::operator+= (const big_integer& bi2) {
	return (*this) = (*this) + bi2;
}

big_integer operator- (const big_integer& bi1, const big_integer& bi2) {
	return bi1 + (-bi2);	
}

big_integer& big_integer::operator-= (const big_integer& bi2) {
	return (*this) = (*this) - bi2;
}

const big_integer big_integer::operator- () const {
	big_integer a(~(*this));
	//add 1
	for (size_t i = 0; i < a.data.size(); i++)
		if (a.data[i] == MAX) {
			a.data.write_at(i) = 0;
		} else {
			a.data.write_at(i)++;
			break;
		}
	return a;
}

const big_integer big_integer::operator+() const {
	return *this;
}

big_integer operator* (const big_integer& bi1, const big_integer& bi2) {
	big_integer res;
	big_integer a(bi1), b(bi2);
	int res_sign = sign(a.data) * sign(b.data);
	ull base = (ull)MAX + 1;
	if (a < 0) {
		a = -a;
	}
	if (b < 0) {
		b = -b;
	}
	set_size(res.data, a.data.size() + b.data.size() + 10);
	for (size_t i = 0; i < a.data.size(); i++)
		for (size_t j = 0; j < b.data.size(); j++) {
			int k = i + j;
			ull x = (ull)a.data[i] * b.data[j];
			while (x > 0) {
				x = x + res.data[k];
				res.data.write_at(k) = (unsigned)x;
				x /= base;
				k++;
			}
		}
	normalize_size(res.data);
	if (res_sign == -1) {
		res = -res;
	}
	return res;
}

big_integer& big_integer::operator*= (const big_integer& bi2) {
	return (*this) = (*this) * bi2;
}

pair<big_integer, big_integer> div_and_mod(big_integer a, big_integer b) {
	big_integer res;
	int sz = max(a.data.size(), b.data.size()) + 1;
	set_size(a.data, sz);
	set_size(res.data, sz);
	a.data.reverse();
	b.data.reverse();

	int en = WORD_SIZE * (a.data.size() - b.data.size());
	for (int i = 0, res_bit = en; i <= en; i++, res_bit--) {
		int can = 1;
		for (size_t j = 0; j < b.data.size(); j++) {
			unsigned o1 = get_block(a.data, i + WORD_SIZE * j), o2 = b.data[j];
			if (o1 < o2) {
				can = 0;
				break;
			} else if (o1 > o2) {
				break;
			}
		}
		
		if (!can) continue;
		inv_set_bit(res.data, res_bit, 1);
		ll cur = 0;
		bool need = 0;
		ll base = (ll)MAX + 1;
		for (int j = b.data.size() - 1; j >= 0; j--) {
			cur = (ll)get_block(a.data, i + WORD_SIZE* j) - b.data[j] - need;
			if (cur < 0) {
				cur += base;
				need = 1;
			} else {
				need = 0;
			}
			set_block(a.data, i + WORD_SIZE * j, (unsigned)cur);
		}
	}
	
	a.data.reverse();
	normalize_size(a.data);
	normalize_size(res.data);
	return make_pair(res, a);
}

big_integer operator/ (const big_integer& bi1, const big_integer& bi2) {
	int res_sign = sign(bi1.data) * sign(bi2.data);
	big_integer a, b;
	if (bi1 < 0) {
		a = -bi1;
	} else {
		a = bi1;
	}
	if (bi2 < 0) {
		b = -bi2;
	} else {
		b = bi2;
	}
	big_integer res = div_and_mod(a, b).first;
	if (res_sign == -1) return -res;
	else return res;
}

big_integer& big_integer::operator/= (const big_integer& bi2) {
	return (*this) = (*this) / bi2;
}

big_integer operator% (const big_integer& bi1, const big_integer& bi2) {
	//если поэкспериментировать то остаток от делени€ дл€ интов просто равен остатку от делени€ модулей,
	//на знак делимого, сделаем также и дл€ бигинтов
	int res_sign = sign(bi1.data);
	big_integer a, b;
	if (bi1 < 0) {
		a = -bi1;
	} else {
		a = bi1;
	}
	if (bi2 < 0) {
		b = -bi2;
	} else {
		b = bi2;
	}
	big_integer res = div_and_mod(a, b).second;
	if (res_sign == -1) return -res;
	else return res;
}

big_integer& big_integer::operator%= (const big_integer& bi2) {
	return (*this) = (*this) % bi2;
}

//---------------------------------------------------------

big_integer operator& (const big_integer& bi1, const big_integer& bi2)  {
	big_integer a(bi1), b(bi2);
	int sz = max(a.data.size(), b.data.size());
	set_size(a.data, sz);
	set_size(b.data, sz);
	for (int i = 0; i < sz; i++) {
		a.data.write_at(i) = a.data[i] & b.data[i]; 
	}
	normalize_size(a.data);
	return a;
}

big_integer& big_integer::operator&= (const big_integer& bi2) {
	return (*this) = (*this) & bi2;
}

big_integer operator| (const big_integer& bi1, const big_integer& bi2) {
	big_integer a(bi1), b(bi2);
	int sz = max(a.data.size(), b.data.size());
	set_size(a.data, sz);
	set_size(b.data, sz);
	for (int i = 0; i < sz; i++) {
		a.data.write_at(i) = a.data[i] | b.data[i]; 
	}
	normalize_size(a.data);
	return a;
}

big_integer& big_integer::operator|= (const big_integer& bi2) {
	return (*this) = (*this) | bi2;
}

big_integer operator^ (const big_integer& bi1, const big_integer& bi2) {
	big_integer a(bi1), b(bi2);
	int sz = max(a.data.size(), b.data.size());
	set_size(a.data, sz);
	set_size(b.data, sz);
	for (int i = 0; i < sz; i++) {
		a.data.write_at(i) = a.data[i] ^ b.data[i]; 
	}
	normalize_size(a.data);
	return a;
}

big_integer& big_integer::operator^= (const big_integer& bi2) {
	return (*this) = (*this) ^ bi2;
}

const big_integer big_integer::operator~ () const {
	big_integer res(*this);
	for (size_t i = 0; i < data.size(); i++) {
		res.data.write_at(i) = ~res.data[i];
	}
	return res;
}

big_integer& big_integer::operator++() {
	(*this) += 1;
	return *this;
}

const big_integer big_integer::operator++ (int) {
	big_integer tmp(*this);
	(*this) += 1;
	return tmp;
}


big_integer& big_integer::operator--() {
	(*this) -= 1;
	return *this;
}

const big_integer big_integer::operator-- (int) {
	big_integer tmp(*this);
	(*this) -= 1;
	return tmp;
}

//---------------------------------------------------------

big_integer operator<< (const big_integer& a, int x) {
	int i = WORD_SIZE * a.data.size() - 1 + x;
	big_integer res;
	set_size(res.data, i / WORD_SIZE + 1);
	for (; i >= x; i--) {
		if (bit(a.data, i - x)) {
			res.data.write_at(i / WORD_SIZE) += (1 << (i % WORD_SIZE));
		}
	}
	normalize_size(res.data);
	return res;
}

big_integer& big_integer::operator<<= (int x) {
	return (*this) = (*this) << x;
}

big_integer operator>> (const big_integer& a, int x) {
	size_t last_bit = WORD_SIZE * a.data.size() - 1 + x;
	big_integer old(a);
	big_integer res;
	set_size(res.data, last_bit / WORD_SIZE + 1);
	set_size(old.data, last_bit / WORD_SIZE + 1);
	for (size_t i = 0; i <= last_bit - x; i++) {
		if (bit(old.data, i + x)) {
			res.data.write_at(i / WORD_SIZE) += (1 << (i % WORD_SIZE));
		}
	}
	if (old < 0) {
		for (size_t i = last_bit - x + 1; i < WORD_SIZE * (last_bit / WORD_SIZE + 1); i++) {
			res.data.write_at(i / WORD_SIZE) += (1 << (i % WORD_SIZE));
		}
	}
	normalize_size(res.data);
	return res;
}

big_integer& big_integer::operator>>= (int x) {
	return (*this) = (*this) >> x;
}

//----------------------------------------------------------

string to_string(const big_integer& bi) {
	string res = "";
	big_integer b = bi;
	bool negative = false;
	unsigned last = bi.data.back();
	if (sign(bi.data) == -1) {
		negative = true;
		b = -b;
	}
	
	vector<int> ans;
	for (size_t i = b.data.size() - 1; ; i--) {
		for (int j = WORD_SIZE - 1; j >= 0; j--) {
			mul2(ans);
			if ((1 << j) & b.data[i]) {
				add1(ans);
			}
		}
		if (i == 0) break;
	}

	if (ans.size() == 0) return "0";
	if (negative) res = "-";
	for (auto i = ans.rbegin(); i != ans.rend(); i++) {
		res += (*i) + '0';
	}
	return res;
}