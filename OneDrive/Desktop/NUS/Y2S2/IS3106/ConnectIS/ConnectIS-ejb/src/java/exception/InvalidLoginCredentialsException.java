/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package exception;

/**
 *
 * @author erin
 */
public class InvalidLoginCredentialsException extends Exception {

    /**
     * Creates a new instance of <code>InvalidLoginCredentialsException</code>
     * without detail message.
     */
    public InvalidLoginCredentialsException() {
    }

    /**
     * Constructs an instance of <code>InvalidLoginCredentialsException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidLoginCredentialsException(String msg) {
        super(msg);
    }
}
