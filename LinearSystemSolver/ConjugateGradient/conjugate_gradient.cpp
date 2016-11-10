#include <iostream>
#include <fstream>
#include <string>
#include "../misc/matrix.h"

using namespace std;

ostream & operator<<(ostream & os, Matrix const & M)
{
    for(size_t i = 0; i < M.height(); ++i)
    {
        for(size_t j = 0; j < M.width(); ++j)
            os << M[i][j] << " ";
        os << endl;
    }
    return os;
}

Matrix get_gradient(Matrix const & M, Matrix const & Free, Matrix const & P)
{
    return (M * P) - Free;
}

double scalar_product(Matrix const & A, Matrix const & B)
{
    if(A.width() != 1 || B.width() != 1)
		throw std::runtime_error("A.width != 1 || B.width != 1");
	if(A.height() != B.height())
		throw std::runtime_error("A.height != B.height");

    double res = 0.0;
    for(size_t i = 0; i < A.height(); ++i)
        res += A[i][0] * B[i][0];
    return res;
}

double get_alpha(Matrix const & M, Matrix const & P, Matrix const & gradient)
{
    double alpha = -scalar_product(gradient, P) / scalar_product(M * P, P);
//    cout << "alpha: " << alpha << endl;
    return alpha;
}

double get_beta(Matrix const & M, Matrix const & P, Matrix const & gradient)
{
    Matrix A = M * P;
//    cout << "A: " << endl << A;
    double beta = scalar_product(A, gradient) / scalar_product(A, P);
//    cout << "beta: " << beta << endl;
    return beta;
}

Matrix get_next_P(Matrix const & M, Matrix const & P, Matrix const & gradient)
{
    return (P * get_beta(M, P, gradient)) - gradient;
}

bool convergence(Matrix const & M, Matrix const & Free, Matrix solution, double eps)
{
	solution = M * solution;
	for(size_t i = 0; i < solution.height(); ++i)
		if(! fabs(solution[i][0] - Free[i][0]) < eps)
			return false;

	return true;
}

Matrix conjugate_gradient(const Matrix & M, const Matrix & Free, Matrix solution, double eps, size_t iterations, ostream & flog)
{
    if (M.width() != M.height())
        throw std::runtime_error("M.width != M.height");
    if (Free.width() != 1)
        throw std::runtime_error("Free.width != 1");
    if (Free.height() != M.width())
        throw std::runtime_error("Free.height != M.width");

    Matrix gradient = get_gradient(M, Free, solution);
    Matrix P = gradient * (-1);

//    cout << "solution: " << endl << solution;
//    cout << "P: " << endl << P << endl;

    size_t i;
    for(i = 0; i < iterations && !convergence(M, Free, solution, eps); ++i)
    {
        solution = solution + (P * get_alpha(M, P, gradient));
        gradient = get_gradient(M, Free, solution);
        P = get_next_P(M, P, gradient);
//        cout << "solution: " << endl << solution;
//        cout << "P: " << endl << P << endl;
    }
    flog << i << endl;
    return solution;
}

void solve(ofstream & flog)
{
	ofstream fout("out_conjugate_gradient");
	ifstream fin("input");

	while(!fin.eof())
	{
		Matrix A, B, approx;
		size_t iterations;
		double eps;
		string to_parse;

		for(size_t tmp = 0; tmp < 4; ++tmp)
		{
			fin >> to_parse;
			if(fin.eof())
				return;
			if(to_parse == "Matrix")
			{
				size_t n;
				fin >> n;
				A.set_height(n);
				A.set_width(n);
				B.set_height(n);
				B.set_width(1);
				approx.set_height(n);
				approx.set_width(1);
				for(size_t i = 0; i < n; ++i)
				{
					for(size_t j = 0; j < n; ++j)
						fin >> A[i][j];
					fin >> B[i][0];
				}
			}
			else if(to_parse == "Epsilon")
				fin >> eps;
			else if(to_parse == "MaxIteration")
				fin >> iterations;
			else if(to_parse == "InitApproximation")
				for(size_t i = 0; i < approx.height(); ++i)
					fin >> approx[i][0];
		}

        Matrix solution = conjugate_gradient(A, B, approx, eps, iterations, flog);

		for(size_t i = 0; i < solution.height(); ++i)
			fout << solution[i][0] << " ";
		fout << endl;
	}
}

int main()
{
	ofstream flog("log_conjugate_gradient");

    try
	{
		solve(flog);
	}
	catch(exception & e)
	{
		flog << e.what() << endl;
	}

	return 0;
}
