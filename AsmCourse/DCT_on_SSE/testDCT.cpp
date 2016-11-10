#include <iostream>
#include <cmath>
#include <cstdlib>
#include <cstdio>

extern "C" {

    void DCT(float *in, float *out, int count);

    void IDCT(float *in, float *out, int count);
}

const int count_test = 100000;
const int size = 8;
const int size_of_test = size * size;
float max_number = 100;

float random_float() {
    float res = (float)rand()/RAND_MAX;
    return res;
}

void gen_test(float *mem) {
    for (int i = 0; i < size_of_test; i++) {
        *(mem + i) = random_float() * max_number;
    }
}

void print_test(float *mem) {
    return;
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            printf("%.3f ", *mem);
            mem++;
        }
        puts("");
    }
    puts("-----------------------------------------------------");
}

void solve_test(float *in, float *out) {
    float tmp[size][size];
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            tmp[i][j] = 0;
            for (int k = 0; k < size; k++) {
                tmp[i][j] += in[i * size + k] * cos(acos(0) / 8 * (2 * k + 1) * j);
            }
        }
    }
    for (int j = 0; j < size; j++) {
        for (int i = 0; i < size; i++) {
            out[i * size + j] = 0;
            for (int k = 0; k < size; k++) {
                out[i * size + j] += tmp[k][j] * cos(acos(0) / 8 * (2 * k + 1) * i);
            }
        }
    }
}
const int count = size_of_test * count_test;
float data1[count];
float data2[count];
float data3[count];
int main() {
    srand(22);
    std::cout << "Let start testing" << std::endl;
    float *in = data1;
    float *out = data2;
    float *right = data3;
    for (int test = 0; test < count_test; test++) {
        float *addres = in + size_of_test * test;
        gen_test(addres);
        print_test(addres);
        solve_test(addres, right);
        right += size_of_test;
    }
    for (int test = 0; test < count_test * size_of_test; test++) {
        *(out + test) = 0;
    }
    printf("start\n");
    DCT(in, out, count_test);
     for (int test = 0; test < count_test; test++) {
        float *addres = out + size_of_test * test;
        print_test(addres);
    }
   IDCT(out, in, count_test);
    printf("finish\n");
    for (int test = 0; test < count_test; test++) {
        float *addres = in + size_of_test * test;
        print_test(addres);
    }
}
