package baron.tasks;

import baron.exceptions.BaronException;
import baron.messages.Messages;
import baron.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TaskManager {
    private final ArrayList<Task> taskList;

    public TaskManager() {
        this.taskList = new ArrayList<>();
    }

    public boolean isEmpty() {
        return this.taskList.isEmpty();
    }

    public int getTaskCount() {
        return this.taskList.size();
    }

    public Task addTask(Task newTask) {
        this.taskList.add(newTask);
        return newTask;
    }

    public Task addTask(TaskType taskType, String commandArg) throws BaronException {
        Task newTask;
        if (taskType == TaskType.TODO) {
            newTask = new ToDo(commandArg);
        } else if (taskType == TaskType.DEADLINE) {
            String[] splitString = commandArg.split(" /by ", 2);
            if (splitString.length == 2) {
                LocalDateTime localDateTime = DateTimeUtil.getDateTime(splitString[1]);
                newTask = new Deadline(splitString[0], localDateTime);
            } else if (splitString.length == 1) {
                if (splitString[0].startsWith("/by")) {
                    throw new BaronException(Messages.generateEmptyDescMessage(taskType));
                } else {
                    throw new BaronException("Please specify a deadline by the /by keyword.");
                }
            } else  {
                throw new BaronException("Please specify only one deadline.");
            }
        } else {
            String[] splitString = commandArg.split(" /at ", 2);
            if (splitString.length == 2) {
                LocalDateTime localDateTime = DateTimeUtil.getDateTime(splitString[1]);
                newTask = new Event(splitString[0], localDateTime);
            } else if (splitString.length == 1) {
                if (splitString[0].startsWith("/at")) {
                    throw new BaronException(Messages.generateEmptyDescMessage(taskType));
                }
                throw new BaronException("Please specify a date by the /at keyword.");
            } else  {
                throw new BaronException("Please specify only one date.");
            }
        }

        this.taskList.add(newTask);
        return newTask;
    }

    public Task getTask(int index) {
        return this.taskList.get(index - 1);
    }

    public boolean markTask(int index) throws BaronException {
        if (this.isEmpty()) {
            throw new BaronException(Messages.MESSAGE_NO_TASK);
        } else if (index > 0 && index <= this.getTaskCount()) {
            if (this.taskList.get(index - 1).mark()) {
                return true;
            } else {
                throw new BaronException("The task is already marked.");
            }
        } else {
            throw new BaronException("The task index is invalid, only accepts 1 to "
                    + this.getTaskCount() + ".");
        }
    }

    public boolean unmarkTask(int index) throws BaronException {
        if (this.isEmpty()) {
            throw new BaronException(Messages.MESSAGE_NO_TASK);
        } else if (index > 0 && index <= this.getTaskCount()) {
            if (this.taskList.get(index - 1).unmark()) {
                return true;
            } else {
                throw new BaronException("The task is not marked.");
            }
        } else {
            throw new BaronException("The task index is invalid, only accepts 1 to "
                    + this.getTaskCount() + ".");
        }
    }

    public Task deleteTask(int index) throws BaronException {
        if (this.isEmpty()) {
            throw new BaronException(Messages.MESSAGE_NO_TASK);
        } else if (index > 0 && index <= this.getTaskCount()) {
            Task deletedTask = this.taskList.get(index - 1);
            this.taskList.remove(index - 1);
            return deletedTask;
        } else {
            throw new BaronException("The task index is invalid, only accepts 1 to "
                    + this.getTaskCount() + ".");
        }
    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < this.getTaskCount(); i++) {
            output += "  " + (i + 1) + "." + this.taskList.get(i);

            if (i != this.getTaskCount() - 1) {
                output += "\n";
            }
        }

        return output;
    }
}
