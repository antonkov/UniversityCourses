from math import log

prefix = '../data/'


def count_entropy(filename):
    print('\t' + filename)
    with open(prefix + filename, "rb") as f:
        text = b''.join(f.readlines()) # text - лист с символами файла
        N = len(text)
        byte_alphabet_size = 256
        window_sizes = [1, 2, 3, 4, 8]
        for n in window_sizes:
            count_blocks = N - n + 1
            prob = norm_prob = dict() # отображения из символов алфавита (алфавит зависит от длины окна) в вероятности
            # prob - значение вероятностей для пункта 2 (без учета не встречающихся),
            # norm_prob - значение вероятностей для пункта 3 (вероятности для не встречающихся равны 1 / (N ** n))
            for i in range(count_blocks):
                s = text[i:i+n]
                if s in prob:
                    prob[s] += 1
                else:
                    prob[s] = 1

            missing = (byte_alphabet_size ** n) - len(prob)
            # missing = количество не встречающихся символов алфавита
            missing_prob = missing / (N ** n) # доля вероятности приходящаяся на эти символы
            norm_multiplier = 1 - missing_prob # коэффициент нормировки для оставшихся вероятностей

            for i in prob.keys():
                prob[i] /= count_blocks #
                norm_prob[i] = prob[i] * norm_multiplier

            entropy = norm_entropy = 0.0
            # entropy - значение энтропии для пункта 2 (без учета не встречающихся),
            # norm_entropy - значение энтропии для пункта 3 (вероятности для не встречающихся равны 1 / (N ** n))

            for i in prob.values():
                entropy -= i * log(i, 2)

            for i in prob.values():
                norm_entropy -= i * log(i, 2)
            norm_entropy += missing_prob * log((N ** n), 2) # все не встречающиеся имеют одинаковый логарифм в сумме
            # вынесен минус из логарифма из-за проблем с точностью вещественных чисел

            # делим на количество символов в окне, для получения энтропии одного символа
            entropy /= n
            norm_entropy /= n

            # энтропии на букву, нижняя оценка на количество байт
            print("%d | %.5f | %.5f | %6.0f bytes" %(n, entropy, norm_entropy, (norm_entropy * len(text)) / 8))
    print()

if __name__ == '__main__':
    files = ['book2', 'kennedy.xls', 'progp', 'sum']
    for file in files:
        count_entropy(file)