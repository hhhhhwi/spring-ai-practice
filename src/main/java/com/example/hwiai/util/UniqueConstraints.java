package com.example.hwiai.util;

/**
 * 데이터베이스 유니크 제약 조건 정의.
 * 제약 조건 이름과 컬럼명을 중앙에서 관리합니다.
 */
public enum UniqueConstraints {
    UK_EVALUATION_REVIEW_CHARACTERISTIC(
        UniqueConstraintNames.UK_EVALUATION_REVIEW_CHARACTERISTIC,
        new String[]{"review_id", "characteristic_id"}
    ),
    UK_ENTITY_COLUMN1_COLUMN2(
        UniqueConstraintNames.UK_ENTITY_COLUMN1_COLUMN2,
        new String[]{"column1", "column2"}
    );

    private final String constraintName;
    private final String[] columnNames;

    UniqueConstraints(String constraintName, String[] columnNames) {
        this.constraintName = constraintName;
        this.columnNames = columnNames;
    }
    
    public String getConstraintName() {
        return constraintName;
    }
    
    public String[] getColumnNames() {
        return columnNames;
    }
    
    /**
     * 제약 조건 이름으로 컬럼명 배열을 조회합니다.
     * @param constraintName DB 제약 조건 이름 (대소문자 무관)
     * @return 컬럼명 배열, 찾지 못하면 null
     */
    public static String[] getColumnNamesByConstraintName(String constraintName) {
        if (constraintName == null) {
            return null;
        }
        
        String lowerConstraintName = constraintName.toLowerCase();
        for (UniqueConstraints constraint : UniqueConstraints.values()) {
            if (lowerConstraintName.contains(constraint.constraintName.toLowerCase())) {
                return constraint.columnNames;
            }
        }
        
        return null;
    }
    
    /**
     * @Table 어노테이션에서 사용할 컴파일 타임 상수.
     * 어노테이션 속성에는 constant expression만 허용되므로 별도 클래스로 관리합니다.
     */
    public static final class UniqueConstraintNames {
        private UniqueConstraintNames() {}
        
        public static final String UK_EVALUATION_REVIEW_CHARACTERISTIC = "uk_evaluation_review_characteristic";
        public static final String UK_ENTITY_COLUMN1_COLUMN2 = "uk_entity_column1_column2";
    }
}
