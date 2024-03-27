/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package exception;

/**
 *
 * @author erin
 */
public class StudentExistsException extends Exception {

    /**
     * Creates a new instance of <code>StudentExistsException</code> without
     * detail message.
     */
    public StudentExistsException() {
    }

    /**
     * Constructs an instance of <code>StudentExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public StudentExistsException(String msg) {
        super(msg);
    }
}
