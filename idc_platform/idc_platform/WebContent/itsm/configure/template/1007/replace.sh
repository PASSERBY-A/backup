for i in `ls`
do
echo $i
perl -p -i -e 's/\[fld_task_create_by\,/\[fld_req_no\,/g' $i
done
