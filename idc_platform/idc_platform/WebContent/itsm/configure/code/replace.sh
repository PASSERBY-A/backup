for i in `ls`
do
echo $i
perl -p -i -e 's/com\.linkage\.toptea/com\.hp\.idc/g' $i
done
