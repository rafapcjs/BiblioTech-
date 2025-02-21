package com.bookLibrary.rafapcjs.commons.utils.pageable;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


public class pageableUtil {
    public static Pageable createPageable(int page, int size, String sortBy, String direction) {
        return PageRequest.of(page, size,
                direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
    }
}
