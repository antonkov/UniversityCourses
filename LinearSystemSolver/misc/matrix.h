#include <vector>
#include <stdexcept>
#include <cmath>

struct Matrix
{
	Matrix(size_t h, size_t w)
		: h(h), w(w)
		, data(h, std::vector<double>(w, 0.0))
	{}

	Matrix(const std::vector < std::vector <double> > & data)
		: h(data.size()), data(data)
	{
		if(!data.empty())
			w = data[0].size();
		else
			w = 0;
	}

	Matrix() : h(0.0), w(0.0) {}

	double norm() const
	{
		double res = 0;
		for(size_t i = 0; i < h; ++i)
		{
			double cur = 0;
			for(size_t j = 0; j < w; ++j)
				cur += fabs(data[i][j]);
			res = std::max(res, cur);
		}
		return res;
	}

	size_t height() const { return h; }
	size_t width() const { return w; }

	void set_height(size_t n)
	{
		h = n;
		data.resize(h);
		for(size_t i = 0; i < h; ++i)
			data[i].resize(w);
	}

	void set_width(size_t n)
	{
		w = n;
		for(size_t i = 0; i < h; ++i)
			data[i].resize(w);
	}

	std::vector <double> & operator[](size_t i)
	{
		return data[i];
	}

	const std::vector <double> & operator[](size_t i) const
	{
		return data[i];
	}

	//Matrix inverse() const
	//{
	//	if(h != w)
	//		throw std::runtime_error("h != w");
	//	size_t n = h;
	//	Matrix E(n, n);
	//	for(size_t i = 0; i < n; ++i)
	//		E[i][i] = 1.0;
	//	Matrix A(*this);

	//	for(size_t i = 0; i < n; ++i)
	//	{
	//		if(A[i][i] == 0.0)
	//			for(size_t j = i + 1; j < n; ++j)
	//				if(A[i][j] != 0.0)
	//				{
	//					swap(A[i], A[j]);
	//					break;
	//				}
	//
	//		double cur = A[i][i];
	//		for(size_t j = 0; j < n; ++j)
	//		{
	//			A[i][j] /= cur;
	//			E[i][j] /= cur;
	//		}

	//		for(size_t j = 0; j < n; ++j)
	//		{
	//			if(j == i)
	//				continue;
	//			cur = A[j][i];
	//			for(size_t k = 0; k < n; ++k)
	//			{
	//				A[j][k] -= cur * A[i][j];
	//				E[j][k] -= cur * E[i][j];
	//			}
	//		}
	//	}

	//	return E;
	//}

private:
	size_t h, w;
	std::vector < std::vector <double> > data;
};

Matrix operator*(const Matrix & A, const Matrix & B)
{
	if(A.width() != B.height())
		throw std::runtime_error("A.width != B.height");

	Matrix C(A.height(), B.width());
	for(size_t i = 0; i < A.height(); ++i)
		for(size_t j = 0; j < A.width(); ++j)
			for(size_t k = 0; k < B.width(); ++k)
				C[i][k] += A[i][j] * B[j][k];
	return C;
}

Matrix operator+(const Matrix & A, const Matrix & B)
{
	if(A.width() != B.width())
		throw std::runtime_error("A.width != B.width");
	if(A.height() != B.height())
		throw std::runtime_error("A.height != B.height");

	Matrix C(A.height(), A.width());
	for(size_t i = 0; i < A.height(); ++i)
		for(size_t j = 0; j < A.width(); ++j)
			C[i][j] = A[i][j] + B[i][j];
	return C;
}

Matrix operator-(const Matrix & A, const Matrix & B)
{
	if(A.width() != B.width())
		throw std::runtime_error("A.width != B.width");
	if(A.height() != B.height())
		throw std::runtime_error("A.height != B.height");

	Matrix C(A.height(), A.width());
	for(size_t i = 0; i < A.height(); ++i)
		for(size_t j = 0; j < A.width(); ++j)
			C[i][j] = A[i][j] - B[i][j];
	return C;
}

Matrix operator*(Matrix A, double val)
{
	for(size_t i = 0; i < A.height(); ++i)
		for(size_t j = 0; j < A.width(); ++j)
			A[i][j] *= val;
    return A;
}
