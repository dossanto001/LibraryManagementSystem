package swt.hse.de;

public class PatternChecking {

    private String numberRegexPattern = "[+-]?([0-9]*[.])?[0-9]+";
    private String letterRegexPattern = "^[a-zA-Z]*$";
    private String alphaNumericRegexPattern = "^[a-zA-Z0-9]*$";


    public boolean checkNumber(String number){
        return number.matches(numberRegexPattern);
    }

    public boolean checkWord(String word){
        return word.matches(letterRegexPattern);
    }

    public boolean checkAlphaNumeric(String phrase){
        return phrase.matches(alphaNumericRegexPattern);
    }

    public boolean checkYear(String number){
        return (number.length()==4 && checkNumber(number));
    }
}
