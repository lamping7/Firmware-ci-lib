// ROS tests

def getROSTests() {
    def tests = [
        [
            name: "ROS vtol mission new 1",
            test: "mavros_posix_test_mission.test",
            mission: "vtol_new_1",
            vehicle: "standard_vtol"
        ],
        [
            name: "ROS vtol mission new 2",
            test: "mavros_posix_test_mission.test",
            mission: "vtol_new_2",
            vehicle: "standard_vtol"
        ],
        [
            name: "ROS vtol mission old 1",
            test: "mavros_posix_test_mission.test",
            mission: "vtol_old_1",
            vehicle: "standard_vtol"
        ],
        [
            name: "ROS vtol mission old 2",
            test: "mavros_posix_test_mission.test",
            mission: "vtol_old_2",
            vehicle: "standard_vtol"
        ],
        [
            name: "ROS MC mission box",
            test: "mavros_posix_test_mission.test",
            mission: "multirotor_box",
            vehicle: "iris"
        ],
        [
            name: "ROS offboard att",
            test: "mavros_posix_tests_offboard_attctl.test",
            mission: "",
            vehicle: "iris"
        ],
        [
            name: "ROS offboard pos",
            test: "mavros_posix_tests_offboard_posctl.test",
            mission: "",
            vehicle: "iris"
        ]
    ]

    return tests
}