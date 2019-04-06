package schrader.guava.test;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class OptionalTests {

    @Test
    public void of() {
        Optional<String> o = Optional.of("uno");
        assertThat(o.isPresent()).isTrue();
    }

    @Test
    public void ofWithNullValue() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> Optional.of(null));
    }

    @Test
    public void fromNullable() {
        Optional<String> o = Optional.fromNullable(null);
        assertThat(o.isPresent()).isFalse();
    }

    @Test
    public void absent() {
        Optional<String> o = Optional.absent();
        assertThat(o.isPresent()).isFalse();
    }

    @Test
    public void get() {
        Optional<String> o = Optional.of("uno");
        assertThat(o.get()).isEqualTo("uno");
    }

    @Test
    public void or() {
        Optional<String> o = Optional.fromNullable(null);
        assertThat(o.or("default")).isEqualTo("default");
    }

    @Test
    public void orNull() {
        Optional<String> o = Optional.fromNullable(null);
        assertThat(o.orNull()).isNull();
    }

    @Test
    public void transform() {
        Optional<String> o = Optional.of("uno");
        Optional<Integer> mo = o.transform(new Function<String, Integer>() {
            @Nullable
            @Override
            public Integer apply(@Nullable String input) {
                return input.length();
            }
        });
        assertThat(mo.isPresent());
        assertThat(mo.get()).isEqualTo(3);
    }

    @Test
    public void presentInstances() {
        List<Optional<String>> o = Lists.newArrayList(
                Optional.of("uno"),
                Optional.absent(),
                Optional.fromNullable("due"),
                Optional.fromNullable(null));
        Iterable<String> presentInstances = Optional.presentInstances(o);
        assertThat(presentInstances).contains("uno", "due");
    }
}
