#Author: Ebrain Mirambeau
#Purpose: gcf function computer greatest common factor of two numbers

def gcf(num1, num2):
    #Input two positive integers (num1, num2)
    #Output: greatest common factor between input
    a = 0
    b = 0
    if (num1 <= num2):
        a = num1
        b = num2
    else:
        a = num2
        b = num1
        
    x = 2
    acc = 1
    
    while (x <= a):
        while (a % (acc * x)) == 0 and (b % (acc * x)) == 0:
            acc = acc * x    
        x = x + 1
    return acc

def main():
    
    print(gcf(3,6))  # --> 3
    print(gcf(8,12)) # --> 4 
    print(gcf(9,81)) # --> 9

    
if __name__ == "__main__":main()
