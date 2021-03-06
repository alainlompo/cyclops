package com.aol.cyclops.functionaljava.hkt.typeclesses.instances;

import static com.aol.cyclops.util.function.Lambda.l1;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.function.Function;

import org.junit.Test;

import com.aol.cyclops.functionaljava.hkt.NonEmptyListType;
import com.aol.cyclops.functionaljava.hkt.typeclassess.instances.NonEmptyListInstances;
import com.aol.cyclops.util.function.Lambda;

import fj.data.List;
import fj.data.NonEmptyList;

public class NonEmptyListsTest {

    @Test
    public void unit(){
        
        NonEmptyListType<String> list = NonEmptyListInstances.unit()
                                     .unit("hello")
                                     .convert(NonEmptyListType::narrowK);
        
        assertThat(list,equalTo(list("hello")));
    }
    @Test
    public void functor(){
        
        NonEmptyListType<Integer> list = NonEmptyListInstances.unit()
                                     .unit("hello")
                                     .then(h->NonEmptyListInstances.functor().map((String v) ->v.length(), h))
                                     .convert(NonEmptyListType::narrowK);
        
        assertThat(list,equalTo(NonEmptyList.fromList(List.list("hello".length())).some()));
    }
    @Test
    public void apSimple(){
        NonEmptyListInstances.zippingApplicative()
            .ap(NonEmptyListType.of(l1(this::multiplyByTwo)),NonEmptyListType.of(1,2,3));
    }
    private int multiplyByTwo(int x){
        return x*2;
    }
    @Test
    public void applicative(){
        
        NonEmptyListType<Function<Integer,Integer>> listFn =NonEmptyListInstances.unit().unit(Lambda.l1((Integer i) ->i*2)).convert(NonEmptyListType::narrowK);
        
        NonEmptyListType<Integer> list = NonEmptyListInstances.unit()
                                     .unit("hello")
                                     .then(h->NonEmptyListInstances.functor().map((String v) ->v.length(), h))
                                     .then(h->NonEmptyListInstances.zippingApplicative().ap(listFn, h))
                                     .convert(NonEmptyListType::narrowK);
        
        assertThat(list,equalTo(list("hello".length()*2)));
    }
    private <T> NonEmptyList<T> list(T... values) {
        return NonEmptyList.fromList(List.list(values)).some();
                
    }
    private  NonEmptyListType<Integer> range(int start,int end){
        return NonEmptyListType.widen(NonEmptyList.fromList(List.range(start,end)).some());
    }
    @Test
    public void monadSimple(){
       NonEmptyListType<Integer> list  = NonEmptyListInstances.monad()
                                      .flatMap(i->range(0,i), NonEmptyListType.of(1,2,3))
                                      .convert(NonEmptyListType::narrowK);
    }
    @Test
    public void monad(){
        
        NonEmptyListType<Integer> list = NonEmptyListInstances.unit()
                                     .unit("hello")
                                     .then(h->NonEmptyListInstances.monad().flatMap((String v) ->NonEmptyListInstances.unit().unit(v.length()), h))
                                     .convert(NonEmptyListType::narrowK);
        
        assertThat(list,equalTo(list("hello".length())));
    }

    @Test
    public void  foldLeft(){
        int sum  = NonEmptyListInstances.foldable()
                        .foldLeft(0, (a,b)->a+b, NonEmptyListType.of(1,2,3,4));
        
        assertThat(sum,equalTo(10));
    }
    @Test
    public void  foldRight(){
        int sum  = NonEmptyListInstances.foldable()
                        .foldRight(0, (a,b)->a+b, NonEmptyListType.of(1,2,3,4));
        
        assertThat(sum,equalTo(10));
    }
    
   
    
}
