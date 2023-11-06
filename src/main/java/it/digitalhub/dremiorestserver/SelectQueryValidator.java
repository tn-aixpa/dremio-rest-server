package it.digitalhub.dremiorestserver;

public class SelectQueryValidator {
    public static void validate(SelectQuery query) throws IllegalArgumentException {
        if (query.getLimit() > 1000) {
            throw new IllegalArgumentException("Size cannot exceed 1000");
        }
        if (query.getOffset() < 0) {
            throw new IllegalArgumentException("Offset must be 0 or greater");
        }
    }
}
