test: testDCT.cpp DCT IDCT
	g++ testDCT.cpp -c -m32
	g++ testDCT.o IDCT.o DCT.o -o test -m32

IDCT: IDCTexpandedloops.asm
	yasm IDCTexpandedloops.asm -o IDCT.o -f elf32 -g dwarf2

DCT: DCTexpandedloops.asm
	yasm DCTexpandedloops.asm -o DCT.o -f elf32 -g dwarf2
