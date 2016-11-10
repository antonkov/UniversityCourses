#ifndef BIG_INTEGER_H
#define BIG_INTEGER_H

#include "big_array.h"
#include <string>

class big_integer {
public:
	big_integer();
	big_integer(const big_integer&);
	big_integer(int);
	explicit big_integer(std::string);
	big_integer& operator= (const big_integer&);

	big_integer& operator+= (const big_integer&);
	big_integer& operator-= (const big_integer&);
	big_integer& operator*= (const big_integer&);
	big_integer& operator/= (const big_integer&);
	big_integer& operator%= (const big_integer&);

	big_integer& operator&= (const big_integer&);
	big_integer& operator|= (const big_integer&);
	big_integer& operator^= (const big_integer&);
	
	big_integer& operator<<= (int);
	big_integer& operator>>= (int);

	const big_integer operator~ () const;
	const big_integer operator- () const;
	const big_integer operator+ () const;

	big_integer& operator++(); //префиксна форма
	const big_integer operator++ (int); //постфиксная форма
	big_integer& operator--(); //префиксна форма
	const big_integer operator-- (int); //постфиксная форма

	friend std::string to_string(const big_integer&);

	friend big_integer operator<< (const big_integer&, int);
	friend big_integer operator>> (const big_integer&, int);

	friend bool operator< (const big_integer&, const big_integer&);
	friend bool operator<= (const big_integer&, const big_integer&);
	friend bool operator> (const big_integer&, const big_integer&);
	friend bool operator>= (const big_integer&, const big_integer&);
	friend bool operator== (const big_integer&, const big_integer&);
	friend bool operator!= (const big_integer&, const big_integer&);

	friend big_integer operator+ (const big_integer&, const big_integer&);
	friend big_integer operator- (const big_integer&, const big_integer&);
	friend big_integer operator* (const big_integer&, const big_integer&);
	friend big_integer operator/ (const big_integer&, const big_integer&);
	friend big_integer operator% (const big_integer&, const big_integer&);

	friend big_integer operator& (const big_integer&, const big_integer&);
	friend big_integer operator| (const big_integer&, const big_integer&);
	friend big_integer operator^ (const big_integer&, const big_integer&);

private:
	friend std::pair<big_integer, big_integer> div_and_mod(big_integer, big_integer);
	big_array data;
};

std::string to_string(const big_integer&);

big_integer operator<< (const big_integer&, int);
big_integer operator>> (const big_integer&, int);

bool operator< (const big_integer&, const big_integer&);
bool operator<= (const big_integer&, const big_integer&);
bool operator> (const big_integer&, const big_integer&);
bool operator>= (const big_integer&, const big_integer&);
bool operator== (const big_integer&, const big_integer&);
bool operator!= (const big_integer&, const big_integer&);

big_integer operator+ (const big_integer&, const big_integer&);
big_integer operator- (const big_integer&, const big_integer&);
big_integer operator* (const big_integer&, const big_integer&);
big_integer operator/ (const big_integer&, const big_integer&);
big_integer operator% (const big_integer&, const big_integer&);

big_integer operator& (const big_integer&, const big_integer&);
big_integer operator| (const big_integer&, const big_integer&);
big_integer operator^ (const big_integer&, const big_integer&);
	
#endif // BIG_INTEGER_H