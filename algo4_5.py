import re

def suffix_list(s):
    n = len(s)
    return [s[i:n] for i in range(len(s))]


def classify(suffix_list, index):
    ret = {}
    s_head = [s for s in suffix_list if len(s) <= index-1]

    for s in s_head:
        ret[s] = [s]

    s_tail = [s for s in suffix_list if len(s) > index-1]

    if s_tail == []:
        out = [ret[s] for s in sorted(ret.keys())]
        out = [l for l in out if l != []]

    for s in s_tail:
        if s[0:index] not in ret:
            ret[s[0:index]] = [s]
        else:
            a = ret[s[0:index]]
            a.append(s)
            a.sort(key = lambda s: len(s))
            ret[s[0:index]] = a

    out = [ret[s] for s in sorted(ret.keys())]
    out = [l for l in out if l != []]
    return out


file_path = 'input.txt'
input_string = open(file_path).readline().rstrip()

pattern = re.compile(r"[][\'\,]")
max_len =  max([len(s) for s in suffix_list(input_string)])

H = 1
suffices = classify(suffix_list(input_string), 1)
print 'H: ' + str(H)
for s in suffices:
    print pattern.sub("", str(s))

while (H*2 < max_len):

    H = H * 2
    suffices = sum([classify(s, H) for s in suffices], [])

    print '\nH: ' + str(H)
    for s in  suffices:
        print pattern.sub("", str(s))