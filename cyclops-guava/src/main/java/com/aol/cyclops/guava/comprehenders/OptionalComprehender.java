package com.aol.cyclops.guava.comprehenders;

import java.util.function.Function;

import com.aol.cyclops.types.extensability.Comprehender;
import com.aol.cyclops.types.extensability.ValueComprehender;
import com.google.common.base.Optional;

public class OptionalComprehender implements ValueComprehender<Optional> {

    @Override
    public Object resolveForCrossTypeFlatMap(Comprehender comp, Optional apply) {
        if (apply.isPresent())
            return comp.of(apply.get());
        return comp.empty();
    }

    @Override
    public Object map(Optional t, Function fn) {
        return t.transform(r -> fn.apply(r));
    }

    @Override
    public Object flatMap(Optional t, Function fn) {
        java.util.Optional o = java.util.Optional.empty();
        if (t.isPresent())
            o = java.util.Optional.of(t.get());

        java.util.Optional res = o.flatMap(r -> unwrap((Optional) fn.apply(r)));
        Optional ret = Optional.absent();
        if (res.isPresent())
            ret = Optional.of(res.get());
        return ret;

    }

    private java.util.Optional unwrap(Optional res) {

        java.util.Optional ret = java.util.Optional.empty();
        if (res.isPresent())
            ret = java.util.Optional.of(res.get());
        return ret;
    }

    @Override
    public Optional of(Object o) {
        return Optional.of(o);
    }

    @Override
    public Optional empty() {
        return Optional.absent();
    }

    @Override
    public Class getTargetClass() {
        return Optional.class;
    }

}
