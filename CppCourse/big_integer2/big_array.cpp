#include "big_array.h"
#include <vector>
#include <algorithm>

big_array::big_array() {
	is_empty = true;
}

big_array::big_array(const big_array& ba) {
	if (ba.is_empty) {
		is_empty = true;
	} else if (ba.small) {
		small = true;
		small_object = ba.small_object;
		is_empty = false;
	} else {
		cnt_references = ba.cnt_references;
		(*cnt_references)++;
		small = false;
		v = ba.v;
		is_empty = false;
	}
}

big_array::~big_array() {
	if (is_empty || small) return;
	clear_in_heap();
}

//read functions
const unsigned& big_array::operator[] (size_t i) const {
	if (small) return small_object;
	else return (*v)[i];
}

size_t big_array::size() const {
	if (is_empty) return 0;
	else if (small) return 1;
	else return v->size();
}

unsigned big_array::back() const {
	if (small) return small_object;
	else return v->back();
}
//write functions
void big_array::reverse() {
	if (is_empty || small) return;
	if (*cnt_references != 1) this->modified();
	std::reverse(v->begin(), v->end());
}

void big_array::push_back(unsigned x) {
	if (is_empty) {
		small = true;
		is_empty = false;
		small_object = x;
	} else if (small) {
		v = new std::vector<unsigned>;
		cnt_references = new int(1);
		v->push_back(small_object);
		v->push_back(x);
		small = false;
	} else {
		if (*cnt_references != 1) this->modified();
		v->push_back(x);
	}
}

unsigned& big_array::write_at (size_t i) {
	if (small) {
		return small_object;
	} else {
		if (*cnt_references != 1) this->modified();
		return (*v)[i];
	}
}

void big_array::pop_back() {
	if (small) {
		is_empty = true;
	} else {
		if (*cnt_references != 1) this->modified();
		v->pop_back();
		if (v->size() == 1) {
			small = true;
			small_object = v->front();
			clear_in_heap();
		}
	}
}

void big_array::swap (big_array &ba) {
	big_array tmp(ba);
	//exists by default
	ba = (*this);
	(*this) = tmp;
}

void big_array::modified() {
	(*cnt_references)--;
	cnt_references = new int(1);
	v = new std::vector<unsigned>(*v);
}

void big_array::clear_in_heap() {
	if (*cnt_references == 1) {
		v->clear();
		delete v;
		delete cnt_references;
	} else (*cnt_references)--;
}