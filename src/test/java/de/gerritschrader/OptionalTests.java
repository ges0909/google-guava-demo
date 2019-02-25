package de.gerritschrader;

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
        Optional<String> optional = Optional.of("uno");
        assertThat(optional.isPresent()).isTrue();
    }

    @Test
    public void ofWithNullValue() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> Optional.of(null));
    }

    @Test
    public void fromNullable() {
        Optional<String> optional = Optional.fromNullable(null);
        assertThat(optional.isPresent()).isFalse();
    }

    @Test
    public void absent() {
        Optional<String> optional = Optional.absent();
        assertThat(optional.isPresent()).isFalse();
    }

    @Test
    public void get() {
        Optional<String> optional = Optional.of("uno");
        assertThat(optional.get()).isEqualTo("uno");
    }

    @Test
    public void or() {
        Optional<String> optional = Optional.fromNullable(null);
        assertThat(optional.or("default")).isEqualTo("default");
    }

    @Test
    public void orNull() {
        Optional<String> optional = Optional.fromNullable(null);
        assertThat(optional.orNull()).isNull();
    }

    @Test
    public void transform() {
        Optional<String> optional = Optional.of("uno");
        Optional<Integer> mappedOptional = optional.transform(new Function<String, Integer>() {
            @Nullable
            @Override
            public Integer apply(@Nullable String input) {
                return input.length();
            }
        });
        assertThat(mappedOptional.isPresent());
        assertThat(mappedOptional.get()).isEqualTo(3);
    }

    @Test
    public void presentInstances() {
        List<Optional<String>> optionalList = Lists.newArrayList(
                Optional.of("uno"),
                Optional.absent(),
                Optional.fromNullable("due"),
                Optional.fromNullable(null));
        Iterable<String> presentInstances = Optional.presentInstances(optionalList);
        assertThat(presentInstances).contains("uno", "due");
    }
}
