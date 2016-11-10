#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <string.h> 
extern "C" {
    void dbl2str(double *in, char *out_buf);
}

const int max_size = 32;

typedef unsigned long long ull;
typedef long long ll;

int main() {
    double a = 0;
    double b = 0;
    double x = -19.1231;
    printf("Ready for calc\n");
    char *res = (char *)malloc(max_size);
    dbl2str(&x, res);
    printf("%s\n", res);
    double y = 0;
    ull res1 = (ull)&x;
    ll res2 = (ll)&x;
    ll res3 = (ll)res;
 //   printf("%lld %lld", res2, res3);
//    sscanf(res, "%lf", &y);
//    printf("%s", y == x ? "equal" : "not equal");
}
