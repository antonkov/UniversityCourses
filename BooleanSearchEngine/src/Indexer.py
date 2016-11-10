__author__ = 'antonkov'

import sys
import re
import os.path
import xml.etree.cElementTree as ET
import json

import time
global time0

def print_time(s):
    global time0
    cur_time = time.time()
    print (cur_time - time0), " ", s
    time0 = cur_time

countTokens, countTermins = 0, 0
corpus_path, index_path = "", ""

def read_data(path, ext, data, docNames):
    docId = [0]

    def step(ext, dirname, names):
        ext = ext.lower()

        for name in names:
            if name.lower().endswith(ext):
                try:
                    filename = os.path.join(dirname, name)
                    tree = ET.parse(filename)

                    root = tree.getroot()
                    root = root.find('text').find('body')
                    pars = dict()
                    for par in root:
                        pars[par.get('n')] = par.text

                    data[docId[0]] = pars
                    docNames.append(os.path.relpath(filename, corpus_path))

                    docId[0] = docId[0] + 1
                except:
                    pass

    os.path.walk(corpus_path, step, ext)

if __name__ == "__main__":
    if len(sys.argv) <= 2:
        print "Error. You should set path to corpus and result index"
        sys.exit(1)

    time0 = time.time()

    corpus_path = sys.argv[1]
    index_path = sys.argv[2]

    data, docNames = dict(), []
    read_data(corpus_path, ".xml", data, docNames)
    print_time("read complete")

    terminDocLists, freq = dict(), dict()

    wordsRegexp = re.compile(r'\w+')

    for docId, pars in data.iteritems():
        words = set()
        for par, text in pars.iteritems():
            for word in wordsRegexp.findall(text):
                word = word.lower()
                words.add(word)
                countTokens += 1
        for word in words:
            if not word in terminDocLists:
                terminDocLists[word] = []
            terminDocLists[word].append(docId)
    countTermins = len(terminDocLists)
    for word, list in terminDocLists.iteritems():
        freq[word] = len(list)

    print_time("inverse index building complete")
    print "count tokens = ", countTokens, " count termins = ", countTermins

    # saving
    with open(index_path, 'w') as out_file:
        data = {'terminDocLists':terminDocLists, 'docNames': docNames}
        json.dump(data, out_file)

    print_time("saving complete")