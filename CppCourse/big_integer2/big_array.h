#ifndef BIG_ARRAY_H
#define BIG_ARRAY_H

#include <vector>
#include <algorithm>

class big_array {
public:
	big_array();
	big_array(const big_array&);
	~big_array();
//read functions
	const unsigned& operator[] (size_t) const;

	size_t size() const;

	unsigned back() const;
//write functions
	void reverse();

	void push_back(unsigned);

	unsigned& write_at (size_t);

	void pop_back();

	void swap (big_array&);

	void modified();

	void clear_in_heap();
private:
	unsigned small_object;
	std::vector<unsigned> *v;
	bool small, is_empty;
	int *cnt_references;
};

#endif // BIG_ARRAY_H