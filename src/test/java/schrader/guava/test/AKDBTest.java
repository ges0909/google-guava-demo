package schrader.guava.test;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import org.joda.time.LocalDate;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Nullable;

public class AKDBTest {

    private static final String VERZOGEN_AM = "%s (verzogen am %s nach %s)";
    private static final String WEGZUG_DATUM_UNBEKANNT = "Unbekannt";
    private static final String WEGZUG_ADRESSE_UNBEKANNT = "Unbekannt";
    private static final String DATUM_OHNE_ZEIT = "dd.MM.yyyy";

    @Test
    public void datumPresentAndOrtPresent() {
        String actual = formatiereAusloeser(Optional.of(LocalDate.now()), Optional.of("Berlin"));
        assertThat(actual).isEqualTo("Wegzug (verzogen am 06.04.2019 nach Berlin)");
    }

    @Test
    public void datumAbsentAndOrtPresent() {
        String actual = formatiereAusloeser(Optional.absent(), Optional.of("Berlin"));
        assertThat(actual).isEqualTo("Wegzug (verzogen am Unbekannt nach Berlin)");
    }

    @Test
    public void datumPresentAndOrtAbsent() {
        String actual = formatiereAusloeser(Optional.of(LocalDate.now()), Optional.absent());
        assertThat(actual).isEqualTo("Wegzug (verzogen am 06.04.2019 nach Unbekannt)");
    }

    @Test
    public void datumAbsentAndOrtAbsent() {
        String actual = formatiereAusloeser(Optional.absent(), Optional.absent());
        assertThat(actual).isEqualTo("Wegzug (verzogen am Unbekannt nach Unbekannt)");
    }

    private String formatiereAusloeser(Optional<LocalDate> wegzugsDatum, Optional<String> ort) {
        return String.format(VERZOGEN_AM, "Wegzug", wegzugsDatum.transform(new Function<LocalDate, String>() {
            @Nullable
            @Override
            public String apply(@Nullable LocalDate localDate) {
                return localDate.toString(DATUM_OHNE_ZEIT);
            }
        }).or(WEGZUG_DATUM_UNBEKANNT), ort.or(WEGZUG_ADRESSE_UNBEKANNT));
    }
}
