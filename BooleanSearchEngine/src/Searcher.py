__author__ = 'antonkov'

from pyparsing import infixNotation, opAssoc, Word, alphanums
import sys
import json
import xml.etree.cElementTree as ET
import re
import os.path

import time
global time0

def print_time(s):
    global time0
    cur_time = time.time()
    print cur_time - time0, s
    time0 = cur_time

docNames, terminDocLists = [], dict()
corpus_path, index_path = "", ""

'''
data = []
for a in ["Brutus ", ""]:
    for b in ["Caesar ", ""]:
        for c in ["Antony ", ""]:
            for d in ["Cleopatra ", ""]:
                data.append(a + b + c + d)
'''

class Token(object):
    def __init__(self,t):
        self.token = t[0]
    def good(self):
        res = [set(), True]
        if self.token in terminDocLists:
            res[0] = set(terminDocLists[self.token])
        return res
    def words(self):
        return set([self.token.lower()])
    def __str__(self):
        return self.token
    __repr__ = __str__

class TokenBinOp(object):
    def __init__(self,t):
        self.args = t[0][0::2]
    def words(self):
        return self.args[0].words().union(self.args[1].words())
    def __str__(self):
        sep = " %s " % self.reprsymbol
        return "(" + sep.join(map(str,self.args)) + ")"
    __repr__ = __str__

def doAnd(ops):
    if not ops[0][1] and not ops[1][1]:
        for op in ops:
            op[1] = not op[1]
        res = doOr(ops)
        res[1] = not res[1]
        return res
    else:
        if ops[0][1]:
            fst, snd = ops[0], ops[1]
        else:
            fst, snd = ops[1], ops[0]
        res = set()
        for docId in fst[0]:
            inSnd = docId in snd[0]
            if inSnd == snd[1]:
                res.add(docId)
        return [res, True]

def doOr(ops):
    if ops[0][1] and ops[1][1]:
        res = ops[0][0].union(ops[1][0])
        return [res, True]
    else:
        for op in ops:
            op[1] = not op[1]
        res = doAnd(ops)
        res[1] = not res[1]
        return res

class TokenAnd(TokenBinOp):
    reprsymbol = '&'
    def good(self):
        return doAnd([a.good() for a in self.args])

class TokenOr(TokenBinOp):
    reprsymbol = '|'
    def good(self):
        return doOr([a.good() for a in self.args])

class TokenNot(object):
    def __init__(self,t):
        self.arg = t[0][1]
    def __str__(self):
        return "~" + str(self.arg)
    def words(self):
        return self.arg.words()
    __repr__ = __str__
    def good(self):
        was = self.arg.good()
        was[1] = not was[1]
        return was

token = Word(alphanums)
token.setParseAction(Token)

searchExpr = infixNotation( token,
    [
    ("not", 1, opAssoc.RIGHT, TokenNot),
    ("and", 2, opAssoc.LEFT,  TokenAnd),
    ("or",  2, opAssoc.LEFT,  TokenOr),
    ])

def snippets(words_in_requst, docId):
    wordsRegexp = re.compile(r'\w+')

    words = words_in_request.copy()
    tree = ET.parse(os.path.join(corpus_path, docNames[docId]))
    root = tree.getroot()
    root = root.find('text').find('body')
    snips = []
    pars = []
    for par in root:
        ws = [w.lower() for w in wordsRegexp.findall(par.text)]
        pars.append((par.text, ws))
    while True:
        best_val = 0
        btxt, bws = "", []
        for txt, ws in pars:
            cur = 0
            for word in words:
                if word in ws:
                    cur += 1
            if cur > best_val:
                best_val = cur
                btxt, bws = txt, ws
        if best_val == 0:
            return snips

        snips.append(btxt)

        to_remove = []
        for word in words:
            if word in bws:
                to_remove.append(word)
        for word in to_remove:
            words.remove(word)


if __name__ == "__main__":
    if len(sys.argv) <= 2:
        print "Error. You should set path to corpus and result index"
        sys.exit(1)

    time0 = time.time()

    corpus_path = sys.argv[1]
    index_path = sys.argv[2]

    with open(index_path, 'r') as index_path:
        data = json.load(index_path)
        terminDocLists = data['terminDocLists']
        docNames = data['docNames']

    print_time("read complete")

    tests = ["vaccination",
             "not res",
             "member and not states",
            "create and within",
             "limited",
             "Brutus and not Caesar",
             "Brutus or not Caesar",
             "(Brutus or Caesar) and not (Antony or Cleopatra)"]

    #for t in tests:
    while True:
        print "Search : ",
        t = sys.stdin.readline()
        time0 = time.time()
        parseTree = searchExpr.parseString(t)[0]
        res = parseTree.good()
        words_in_request = parseTree.words()
        print "Searched : ", t
        if res[1]:
            print "Found ", len(res[0]), " results"
            max_results_to_show = 5
            shown = 0
            for docId in res[0]:
                if shown == max_results_to_show:
                    break
                shown += 1
                print shown, ' ', docNames[docId]
                snips = snippets(words_in_request, docId)
                for snip in snips:
                    print '-------|| ', snip
        else:
            print "Your request is negative"
        print "----->>>>>",
        print_time("time")
        print "***********"