package pkgPanel;

import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import java.util.regex.Pattern;

public class JTextFieldLimit extends PlainDocument {

    JTextFieldLimit() {
        super();
    }

    public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
        if (str == null) return;

        if(isNumeric(str)){
            if ((getLength() + str.length()) > 1) {
                super.replace(0,1, str, attr);
            }else {
                super.insertString(offset, str, attr);
            }

        }
    }

    private final Pattern pattern = Pattern.compile("[1-9]+");

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }
}
