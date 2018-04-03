// create Jenkins node to run test

def call(Map test_def) {
	return {
	    node {
            docker.image(dockerImages.getROS()).inside('-e CCACHE_BASEDIR=${WORKSPACE} -v ${CCACHE_DIR}:${CCACHE_DIR}:rw') {
                stage(test_def.name) {
                    try {
                        sh('export')
                        checkout scm
                        sh('make distclean; rm -rf .ros; rm -rf .gazebo')
                        sh('git fetch --tags')
                        sh('ccache -z')
                        sh('make posix_sitl_default')
                        sh('make posix_sitl_default sitl_gazebo')
                        sh('./test/rostest_px4_run.sh ' + test_def.test + ' mission:=' + test_def.mission + ' vehicle:=' + test_def.vehicle)
                        sh('ccache -s')
                        sh('make sizes')
                    }
                    catch (exc) {
                        //copy ROS logs to workspace
                        sh('cp `find /root/.ros/ -name rosunit-*.xml -print -quit` ${WORKSPACE}')
                        sh('cp `find /root/.ros/ -name rostest-*.log -print -quit` ${WORKSPACE}')
                        //re-throw to Jenkins
                        throw(exc)
                    }
                    finally {
                        //process log
                        sh ('./Tools/upload_log.py -q --description "${JOB_NAME}: ${STAGE_NAME}" --feedback "${JOB_NAME} ${CHANGE_TITLE} ${CHANGE_URL}" --source CI `find /root/.ros/ -name *.ulg -print -quit`')
                        sh ('./Tools/ecl_ekf/process_logdata_ekf.py `find /root/.ros/ -name *.ulg -print -quit`')
                        //copy results to workspace
                        sh('cp `find /root/.ros/ -name *.ulg -print -quit` ${WORKSPACE}')
                        sh('cp `find /root/.ros/ -name *.ulg.mdat.csv -print -quit` ${WORKSPACE}')
                        sh('cp `find /root/.ros/ -name *.ulg.pdf -print -quit` ${WORKSPACE}')
                        archiveArtifacts(allowEmptyArchive: true, artifacts: '**/*.ulg*, **/rosunit-*.xml, **/rostest-*.log')
                        //clean up
                        sh('make distclean; rm -rf /root/.ros; rm -rf /root/.gazebo')
                    }
                }
            }
	    }
	}
}