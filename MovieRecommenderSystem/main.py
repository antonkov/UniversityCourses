from math import sqrt


class Data:
    def __init__(self, user_idx, item_idx, rate):
        self.user_idx = user_idx
        self.item_idx = item_idx
        self.rate = rate

    def __str__(self):
        return 'user={}, item={}, rate={}'.format(self.user_idx, self.item_idx, self.rate)

class ToPredict:
    def __init__(self, test_idx, user_idx, item_idx):
        self.test_idx = test_idx
        self.user_idx = user_idx
        self.item_idx = item_idx
        self.rate = 4

train_data, validation_data, test_data = [], [], []

class Classifier:
    def __init__(self):
        self.mu = float(0)
        self.buser = dict()
        self.bitem = dict()
        self.fuser = dict()
        self.fitem = dict()
        self.count_for_user = dict()
        self.count_for_item = dict()
        self.feature_count = 2

    def insert_if_not_exists(self, test):
        if not test.user_idx in self.buser:
            self.buser[test.user_idx] = 0
            self.fuser[test.user_idx] = [0]*self.feature_count
        if not test.item_idx in self.bitem:
            self.bitem[test.item_idx] = 0
            self.fitem[test.item_idx] = [0]*self.feature_count

    def learn(self, train_data):
        old_rmse, rmse = 0, 25
        term_eps = 1e-5
        small_improve = 1e-2
        down_speed = 1e-1
        lam = 0.1

        iter_num = 0
        total = len(train_data)

        rating = dict()

        for t in train_data:
            if not t.user_idx in rating:
                rating[t.user_idx] = dict()
            rating[t.user_idx][t.item_idx] = float(t.rate)
            self.insert_if_not_exists(t)

        while abs(old_rmse - rmse) > term_eps:
            old_rmse = rmse
            rmse = 0

            for user_idx, rates in rating.items():
                for item_idx, rate in rates.items():
                    bu, bi = self.buser[user_idx], self.bitem[item_idx]
                    fu, fi = self.fuser[user_idx], self.fitem[item_idx]
                    err =  rate - (self.mu + bu + bi + sum(p*q for p,q in zip(fu,fi)))
                    rmse += err**2
                    self.mu += down_speed * err
                    self.buser[user_idx] += down_speed * (err - lam * bu)
                    self.bitem[item_idx] += down_speed * (err - lam * bi)
                    for i in range(self.feature_count):
                        self.fuser[user_idx][i] += down_speed * (err*fi[i] - lam*fu[i])
                        self.fitem[item_idx][i] += down_speed * (err*fu[i] - lam*fi[i])

            rmse = sqrt(rmse / total)
            iter_num += 1
            print('Iteration : {}, RMSE : {}'.format(iter_num, rmse))
            if rmse > old_rmse - small_improve:
                down_speed *= 0.66
                small_improve *= 0.5

    def get_rate(self, test):
        self.insert_if_not_exists(test)
        feat_add = sum(p*q for p,q in zip(self.fuser[test.user_idx], self.fitem[test.item_idx]))
        return self.mu + self.buser[test.user_idx] + self.bitem[test.item_idx] + feat_add

classifer = Classifier()

with open('train.csv', 'r') as f:
    f.readline() #title
    train_data = [Data(*line.strip().split(',')) for line in f.readlines()]
    classifer.learn(train_data)

with open('validation.csv', 'r') as f:
    f.readline() #title
    validation_data = [Data(*line.strip().split(',')) for line in f.readlines()]

with open('test-ids.csv', 'r') as f:
    f.readline() #title
    test_data = [ToPredict(*line.strip().split(',')) for line in f.readlines()]


with open('submission0', 'w') as f:
    f.write('id,rating\n')
    for test in test_data:
        test.rate = classifer.get_rate(test)
        f.write('{},{}\n'.format(test.test_idx, test.rate))