package io.github.dlakomy.psq2json.internal

class ConversionFlowSuite {
  // TODO should parse, create fn and check with Oracle
  /*
    impl. is gonna be like this:

      create or replace function animal_to_json(p_something animal)
      return clob
      as
        l_obj json_object_t := json_object_t();
      begin
        l_obj.put('id', p_something.id);
        l_obj.put('name', p_something.name);
        l_obj.put('species', p_something.species);

        return l_obj.to_clob;
      end;
      /
   */
}
