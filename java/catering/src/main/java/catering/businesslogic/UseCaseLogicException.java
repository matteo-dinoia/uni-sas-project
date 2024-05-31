package catering.businesslogic;

public class UseCaseLogicException extends Exception {
    public UseCaseLogicException(){}

    public UseCaseLogicException(String errorMsg){
        super(errorMsg);
    }
}
