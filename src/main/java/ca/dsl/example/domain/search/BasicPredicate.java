package ca.dsl.example.domain.search;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

class BasicPredicate<T> {

  private SearchCriteria criteria;

  BasicPredicate(SearchCriteria criteria) {
    this.criteria = criteria;
  }

  BooleanExpression getPredicate(Class<T> tClass) {
    PathBuilder<T> entityPath = new PathBuilder<>(tClass, "order");

    // Date criteria
    if (this.isDate(criteria.getValue())) {
      DatePath<LocalDate> datePath = entityPath.getDate(criteria.getKey(), LocalDate.class);
      switch (criteria.getOperation()) {
        case ":":
          return datePath.eq(LocalDate.parse(criteria.getValue()));
        case ">":
          return datePath.goe(LocalDate.parse(criteria.getValue()));
        case "<":
          return datePath.loe(LocalDate.parse(criteria.getValue()));
      }
    }

    if (this.isNumber(criteria.getValue())) {
      NumberPath<BigDecimal> datePath = entityPath.getNumber(criteria.getKey(), BigDecimal.class);
      switch (criteria.getOperation()) {
        case ":":
          return datePath.eq(new BigDecimal(criteria.getValue()));
        case ">":
          return datePath.goe(new BigDecimal(criteria.getValue()));
        case "<":
          return datePath.loe(new BigDecimal(criteria.getValue()));
      }
    }

    StringPath stringPath = entityPath.getString(criteria.getKey());
    if (criteria.getOperation().equalsIgnoreCase(":")) {
      return stringPath.containsIgnoreCase(criteria.getValue());
    }

    return null;
  }

  private boolean isDate(String value) {
    try {
      LocalDate.parse(value);
    } catch (DateTimeParseException e) {
      return false;
    }
    return true;
  }

  private boolean isNumber(String value) {
    try {
      new BigDecimal(value);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }
}
