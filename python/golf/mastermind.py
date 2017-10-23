from random import*
a=str(randint(1000,9999))
q='';t=0
while a!=q:q=input();print(q+':','*'*sum(q[i]==a[i]for i in range(4)));t+=1
print("%d turn%s!"%(t,'s'(t>1)))