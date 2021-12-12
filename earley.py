import sys,copy,sys

enqueue=list()
state_set={}

def process_grammar():
    file="grammar.txt"
    grammar = {}
    with open(file,"r+") as rules1:
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

def grammar_rules():
    rules=[]
    file="grammar.txt"
    with open(file,"r+") as ron:
        rs=ron.readlines()
    for rule in rs:
        rule = rule.strip("\n")
        rule = rule.strip()
        rules.append(rule)
    return rules

def process_pos_list(grammar):
    file="grammar.txt"
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
    state_set.update(chart)
    return chart

def scanner(state,index,word,part_of_speech,chart): 
    category=get_category(state)
    cp=get_current_position(state)
    if category in part_of_speech[word]:
        if (' '.join([category,"->",word]),(index[1],index[1]+1)) not in chart[index[1]+1]:
            enqueue.append(' '.join([category,"->",word]))
            chart[index[1]+1].append((' '.join([category,"->",word]),(index[1],index[1]+1)))
    state_set.update(chart)
    return state_set

def advance_state(state):
    if "*" not in state:
        return state
    l=state.split()
    for i in range(len(l)):
        if l[i]=="*"and i+1 < len(l):
            s=swap(l,i,i+1)
            return ' '.join(s)
    s=swap(l,len(l)-2,len(l)-1)
    return ' '.join(s)

def swap(alist, index1, index2):
    temp = alist[index2]
    alist[index2] = alist[index1]
    alist[index1] = temp
    return alist

def completer(state,index,chart):
    cc=get_category(state)
    for ste in chart[index[0]]:
        ste_cp=get_current_position(ste[0])
        adv=advance_state(ste[0])
        if cc==ste_cp and (adv,(ste[1][0],index[1])) not in chart[index[1]]:
            chart[index[1]].append((adv,(ste[1][0],index[1])))
            enqueue.append(adv)
    state_set.update(chart)
    return state_set

def predictor(state,index,grammar,chart): #fix
    s=state
    category=get_category(s)
    cp=get_current_position(state)
    gr=grammar_rules()
    rules=state[0].split()
    adv=advance_state(state)
    adv=get_category(adv)
    states=[]
    for st in gr:
        lst=st.split()
        if lst[0]==cp and (st.replace("->", "-> *"),(index[1],index[1])) not in chart[index[1]]:
            chart[index[1]].append((st.replace("->", "-> *"),(index[1],index[1])))
            enqueue.append(st.replace("->", "-> *"))
    return state_set

def get_grammar_rules(category,grammar):
    rules=[]
    rule_list=grammar_rules()
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

def earley_parse(words):
    g=process_grammar()
    state_set=initialize(words)
    pos=process_pos_list(g)
    state_set[0].append(("<ROOT> -> * S",(0,0)))
    enqueue.append("<ROOT> -> * S")
    w=words.split()
    for i in range(len(w)):
        j=len(state_set[i])
        for lst in state_set[i]:
            cs=copy.copy(lst[0])
            cc=get_category(cs)
            current=get_current_position(lst[0])
            adv=advance_state(copy.copy(cs))
            f=get_category(adv)
            np=get_current_position(adv)
            if not is_state_complete(current,cs) and f not in parts_of_speech_list(pos):
                predictor(lst[0],lst[1],g,state_set)
            elif not is_state_complete(current,cs) and f in parts_of_speech_list(pos):
                scanner(lst[0],lst[1],w[i],pos,state_set)
            else:   
                completer(lst[0],lst[1],state_set)
    return state_set

def get_category(state):
    s=state.split()[0]
    return s

def get_current_position(state):
    print(state)
    s=state.split()[2:]
    v=state.split()
    if "*" not in v:
        return v[len(v)-1]
    if len(s)==1:
        return s[0]
    for i in range(len(s)):
        if s[len(s)-1]!="*":
            if s[i]=="*":
                
                return s[i+1]
    return s[len(s)-2]

def is_state_complete(category,state):
    state=state.split()
    for i in range(len(state)):
        if state[i]==category:
            if state[i-1]=="*":
                return False
    return state[len(state)-1]

def print_states(enqueue):
    for entry in enqueue:
        print("#########CHART "+str(entry)+"#########")
        for state in enqueue[entry]:
            print(state)
        print()
        
def main():
    x=earley_parse(sys.argv[1])
    print_states(x)

if __name__ == "__main__":
    main()
