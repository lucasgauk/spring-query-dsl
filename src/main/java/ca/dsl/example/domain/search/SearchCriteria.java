package ca.dsl.example.domain.search;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class SearchCriteria {

  private static final List<String> OPERATIONS = Arrays.asList("<", ">", ":");

  private String key;
  private String operation;
  private String value;

  Boolean isValid() {
    return key != null && OPERATIONS.contains(operation) && value != null;
  }

}
