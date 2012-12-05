package net.virtualqueues.qoperator.model;

public class DaySchedule {
	/*contains a list of {time, activity} from 00:00 to 23:59 . 00:00 is always in the list
	 * the times are sorted
	example:
		00:00, "not working"
		08:00, "accepting any patients"
		12:00, "not working"
		13:00, "accepting children only"
		17:00, "not working"
		here "not working" until 23:59 is automatically understood
	*/
}
