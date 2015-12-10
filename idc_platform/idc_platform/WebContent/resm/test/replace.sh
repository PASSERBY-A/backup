for i in `ls`
do
echo $i
perl -p -i -e 's/com\.ailk\.toptea/com\.hp\.idc/g' $i
done
