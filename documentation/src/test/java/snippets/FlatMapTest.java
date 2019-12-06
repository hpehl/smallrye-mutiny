package snippets;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public class FlatMapTest {

    @Test
    public void flatMap() {
        Multi<Integer> multi = Multi.createFrom().range(1, 3);
        Uni<Integer> uni = Uni.createFrom().item(1);
        // tag::code[]

        int result = uni
                .map(i -> i + 1)
                .await().indefinitely();

        int result2 = uni
                .flatMap(i -> Uni.createFrom().item(i + 1))
                .await().indefinitely();

        List<Integer> list = multi
                .map(i -> i + 1)
                .collectItems().asList()
                .await().indefinitely();

        List<Integer> list2 = multi
                .flatMap(i -> Multi.createFrom().items(i, i))
                .collectItems().asList()
                .await().indefinitely();

        List<Integer> list3 = multi
                .concatMap(i -> Multi.createFrom().items(i, i))
                .collectItems().asList()
                .await().indefinitely();

        // end::code[]
        assertThat(result).isEqualTo(2);
        assertThat(result2).isEqualTo(2);
        assertThat(list).containsExactly(2, 3);
        assertThat(list2).containsExactly(1, 1, 2, 2);
        assertThat(list3).containsExactly(1, 1, 2, 2);
    }
}