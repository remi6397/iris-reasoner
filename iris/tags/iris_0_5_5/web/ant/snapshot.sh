DIR=/var/www/html/iris-reasoner_org/ant-new/iris
cd $DIR
export CLASSPATH=$CLASSPATH:lib/ext/junit/ant-junit.jar
cvs update -d
ant clean
ant release
#all new files should be editable by the group as well
chmod -R g+w *
rm -rf ../../snapshot/*
#copy the iris-jar and license
cp build/jar/* ../../snapshot/
cp LICENSE.txt ../../snapshot/
#copy required licenses
cp -r lib/ext/jgrapht ../../snapshot
cp -r build/javadoc ../../snapshot/
#and the source code
cp build/iris-src-*.zip ../../snapshot/
#run the junit tests
ant test
chmod -R g+w *
rm -rf ../../snapshot/junit_report
mv build/report/html/ ../../snapshot/junit_report
#and make sure all is group writable
chmod -R g+w ../../snapshot