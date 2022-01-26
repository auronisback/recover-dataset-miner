package it.unina.recoverminer.utilities.enums;

public enum MavenBuildResultEnum {
    SUCCESS(0),
    TEST_ERROR(1),
    FATAL_ERROR(2);

    private final int resultVal;

    MavenBuildResultEnum(int result) {
        this.resultVal=result;
    }

    public static MavenBuildResultEnum valueOfResult(int label) {
        for (MavenBuildResultEnum e : values()) {
            if (e.resultVal ==label) {
                return e;
            }
        }
        return null;
    }
}
