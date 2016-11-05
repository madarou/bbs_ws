# -*- coding: utf-8 -*-
import re
import sys, os, urllib2, json

def get_ip_area(ip):  
    try:  
        apiurl = "http://ip.taobao.com/service/getIpInfo.php?ip=%s" %ip  
        content = urllib2.urlopen(apiurl).read()  
        data = json.loads(content)['data']  
        code = json.loads(content)['code']  
        if code == 0:   # success  
            #print(data['country_id']+' '+data['area']+' '+data['city']+' '+data['region'])    
            return data['country_id']+' '+data['area']+' '+data['city']+' '+data['region']
        else:  
            return '未知'  
    except Exception as ex:  
        print(ex)

def main():
    f=open("C:\\Users\\ZYR\\Desktop\\bbs_ws.txt","r")
    arr={} 
    lines = f.readlines()
    for line in lines:
        line = line.decode('utf-8','ignore')
        #print line
        ipaddress=re.compile(r'^\[INFO\]\s{1}\S+\s{1}\S+\s{1}(.*)={3}$')
        match=ipaddress.match(line)
        if match:
            ip = match.group(1) 
            if(arr.has_key(ip)): 
                arr[ip]+=1
            else: 
                arr.setdefault(ip,1) 
    f.close()
    total=0
    print '====独立ip->访问次数===='
    for key in arr:
        print key+'('+get_ip_area(key)+')'+"->"+str(arr[key])
        total=total+arr[key]

    print '\n====总访问次数===='
    print total

if __name__ == '__main__':
    main()
