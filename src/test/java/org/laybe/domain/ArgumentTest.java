package org.laybe.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.laybe.web.rest.TestUtil;

public class ArgumentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Argument.class);
        Argument argument1 = new Argument();
        argument1.setId(1L);
        Argument argument2 = new Argument();
        argument2.setId(argument1.getId());
        assertThat(argument1).isEqualTo(argument2);
        argument2.setId(2L);
        assertThat(argument1).isNotEqualTo(argument2);
        argument1.setId(null);
        assertThat(argument1).isNotEqualTo(argument2);
    }
}
