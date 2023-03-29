import sys,copy,sys,re
##Author: Ebrain Mirambeau
##Example command line input:
##python3 earley_recognizer.py grammar4.txt "the dog ate the food"


def process_grammar(grammar_file):
    grammar = {}
    with open(grammar_file,"r+") as rules1:
        rules = rules1.readlines()
    for rule in rules:
        rule = rule.strip("\n")
        rule = rule.strip()
        rule_list = rule.split("->")
        lhs=rule_list[0].strip()
        rhs=rule_list[1].strip()
        if lhs not in grammar.keys():
            grammar[lhs]=[rhs]
        else:
            grammar[lhs].extend([rhs])
    return grammar


def grammar_rules(grammar_file):
    rules=[]
    with open(grammar_file,"r+") as ron:
        rs=ron.readlines()
    for rule in rs:
        rule = rule.strip("\n")
        rule = rule.strip()
        rules.append(rule)
    return rules

def process_pos_list(grammar,file):
    part_of_speech = {}
    with open(file,"r+") as rules:
        rules=rules.readlines()
    for rule in rules:
        rule = rule.strip("\n")
        rule = rule.strip()
        rule_list = rule.split("->")
        lhs=rule_list[0].strip()
        rhs=rule_list[1].strip()
        if len(rhs.split())==1 and rhs.split()[0] not in grammar.keys():
            if rhs not in part_of_speech.keys():
                part_of_speech[rhs]=[lhs]
            else:
                part_of_speech[rhs].extend([lhs])
    return part_of_speech

def initialize(words):
    chart = {}
    for i in range(0, len(words.split())+1):
        chart[i]=[]
    return chart

def scanner(state,index,word,part_of_speech,chart,enqueue): 
    category=get_category(state)
    cp=get_current_position(state)
    if category in part_of_speech[word]:
        if (' '.join([category,"->",word,"*"]),(index[1],index[1]+1)) not in chart[index[1]+1]:
            enqueue.append((' '.join([category,"->",word,"*"]),(index[1],index[1]+1)))
            chart[index[1]+1].append((' '.join([category,"->",word,"*"]),(index[1],index[1]+1)))
    return chart

def advance_state(state):
    if "*" not in state:
        return "cannot advance state"
    l=state.split()
    if l[len(l)-1]=="*":
        return "cannot advance state"
    for i in range(len(l)):
        if l[i]=="*"and i+1 < len(l):
            s=swap(l,i,i+1)
            return ' '.join(s)

def swap(alist, index1, index2):
    temp = alist[index2]
    alist[index2] = alist[index1]
    alist[index1] = temp
    return alist

def completer(state,index,chart,word,pos,enqueue):
    cc=get_category(state)
    cp=get_current_position(state[0])

    for ste in chart[index[0]]:
        cp=get_current_position(ste[0])
        if cp == "*" or cp==cc:
            y=advance_state(ste[0])
            if (y,(ste[1][0],index[1])) not in chart[index[1]]:
                chart[index[1]].append((y,(ste[1][0],index[1])))
                enqueue.append((y,(ste[1][0],index[1])))
                symbols = y.split()
    return chart


def predictor(state,index,grammar,chart,grm): 
    s=state
    category=get_category(s)
    cp=get_current_position(state)
    gr=grm
    rules=state[0].split()
    adv=advance_state(state)
    adv=get_category(adv)
    states=[]
    for st in gr:
        lst=st.split()
        if lst[0]==cp:
            if (st.replace("->", "-> *"),(index[1],index[1])) not in chart[index[1]]:
                chart[index[1]].append((st.replace("->", "-> *"),(index[1],index[1])))
    return chart

def get_grammar_rules(category,grammar):
    rules=[]
    rule_list=grm
    for rule in rule_list:
        if category in rule:
            rules.append(rule)
    return rules

def parts_of_speech_list(pos):
    parts=[]
    for key in pos.keys():
        for value in pos[key]:
            parts.append(value)
    return parts

def earley_parse(words,pg,pos_list,grm,enqueue):
    words=words+" #"
    g=pg
    state_set=initialize(words)
    pos=pos_list
    state_set[0].append(("<ROOT> -> * S",(0,0)))
    enqueue.append(("<ROOT> -> * S",(0,0)))
    w=words.split()
    w.insert(0,"#")

    for i in range(0, len(state_set)):
        for lst in state_set[i]:
            cs=copy.copy(lst[0])
            cc=get_category(cs)
            current=get_current_position(lst[0])
            adv=advance_state(copy.copy(cs))
            f=get_category(adv)
            np=get_current_position(adv)
            if not is_state_complete(current,cs) and f not in parts_of_speech_list(pos):
                state_set=predictor(lst[0],lst[1],g,state_set,grm)
            elif not is_state_complete(current,cs) and f in parts_of_speech_list(pos):
                state_set=scanner(lst[0],lst[1],w[i+1],pos,state_set,enqueue)
            else:
                state_set=completer(lst[0],lst[1],state_set,w[i],pos,enqueue)
    
    return state_set

def get_category(state):
    s=state.split()[0]
    return s

def get_current_position(state):
    s=state.split()[2:]
    v=state.split()
    for i in range(len(s)):
        if s[i]=="*" and i<len(s)-1:
            return s[i+1]
    return -1

def is_state_complete(category,state):
    state=state.split()
    for i in range(len(state)):
        if state[i]==category:
            if state[i-1]=="*":
                return False
    return True


def print_states(enqueue,state_set):
    for entry in state_set:
        print("#########CHART "+str(entry)+"#########")
        for state in state_set[entry]:
            print(state)
        print()

def pprint(enqueue):
    for symbol in enqueue:
        print(symbol)
        
def main():
    grammar_file=sys.argv[1]
    enqueue=list()
    state_set={}
    pg=process_grammar(grammar_file)
    pos_list=process_pos_list(pg,grammar_file)
    grm=grammar_rules(grammar_file)
    x=earley_parse(sys.argv[2],pg,pos_list,grm,enqueue)
    pprint(enqueue)
    print_states(enqueue,x)

if __name__ == "__main__":
    main()
