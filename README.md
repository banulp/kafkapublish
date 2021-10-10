# kafkapublish

https://www.daangn.com/articles/

0.
https://www.daangn.com/images?image_index=0&object_type=Article&object_id=206800996

1.
<p id="article-price-nanum" property="schema:price" content="0.0" style="font-size:18px; font-weight:bold;">
            무료나눔
        </p>

2.
<h1 property="schema:name" id="article-title" style="margin-top:0px;">미사용  노트북 ㆍ서류가방드림</h1>

3.
<div id="region-name">성남시 분당구 판교동</div>

{"id":"206800996","title":"노트북","region":"성남시 분당구 판교동"}
{"id":"206908580","title":"와플 팬","region":"성남시 분당구 운중동"}
{"id":"206721398","title":"레고블록","region":"성남시 중원구 성남동"}

#!/bin/bash
mvn clean package -Dmaven.test.skip=true

#!/bin/bash
java -jar -Dspring.profiles.active=ubuntu /home/banulp/kafka/pub/kafkapublish/kafkapublish-0.0.1-SNAPSHOT.jar

wget -q -O - https://www.daangn.com/articles/211788053 | grep -e '무료나눔\|article-title\|nickname\|"region-name'

banulp@banulp:~/myToy/delme$ wget -q -O - https://www.daangn.com/articles/211788053 | grep -e '무료나눔\|article-title\|nickname\|"region-name'
<div id="nickname">일타한</div>
<div id="region-name">광산구 신가동</div>
<h1 property="schema:name" id="article-title" style="margin-top:0px;">모니터 무료로 가져가세요</h1>
무료나눔

한자리수 작게

https://www.daangn.com/articles/29764900 0

https://www.daangn.com/articles/29764900 0


nohup java -jar -Dspring.profiles.active=ubuntu kafkapublish-0.0.1-SNAPSHOT.jar 297655720 &

nohup java -jar -Dspring.profiles.active=ubuntu kafkapublish-0.0.1-SNAPSHOT.jar 29765200 0 &
nohup java -jar -Dspring.profiles.active=ubuntu kafkapublish-0.0.1-SNAPSHOT.jar 29765200 1 &
nohup java -jar -Dspring.profiles.active=ubuntu kafkapublish-0.0.1-SNAPSHOT.jar 29765200 2 &
nohup java -jar -Dspring.profiles.active=ubuntu kafkapublish-0.0.1-SNAPSHOT.jar 29765200 3 &
nohup java -jar -Dspring.profiles.active=ubuntu kafkapublish-0.0.1-SNAPSHOT.jar 29765200 4 &
nohup java -jar -Dspring.profiles.active=ubuntu kafkapublish-0.0.1-SNAPSHOT.jar 29765200 5 &
nohup java -jar -Dspring.profiles.active=ubuntu kafkapublish-0.0.1-SNAPSHOT.jar 29765200 6 &
nohup java -jar -Dspring.profiles.active=ubuntu kafkapublish-0.0.1-SNAPSHOT.jar 29765200 7 &
nohup java -jar -Dspring.profiles.active=ubuntu kafkapublish-0.0.1-SNAPSHOT.jar 29765200 8 &
nohup java -jar -Dspring.profiles.active=ubuntu kafkapublish-0.0.1-SNAPSHOT.jar 29765200 9 &


#!/bin/bash
# 1
#RESULT="`wget -qO- https://www.daangn.com/articles/214497222`"
# -1
#RESULT="`wget -qO- https://www.daangn.com/articles/234497222`"
# 0 분당구 유료
#RESULT="`wget -qO- https://www.daangn.com/articles/213815654`"
# 0 다른데 free
#RESULT="`wget -qO- https://www.daangn.com/articles/214741465`"
# 0 숨겨진 글-1
RESULT="`wget -qO- https://www.daangn.com/articles/213895654`"



if echo "$RESULT" | grep -q '"article-price"'; then
echo "0"
elif echo "$RESULT" | grep -q '<div id="region-name">성남시 분당구'; then
echo "1"
elif [ "$RESULT" == "" ]; then
echo "-1"
else
echo "0"
fi

