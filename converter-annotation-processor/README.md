## Converter Annotation Processor
Using the converter-annotation-processor module, we can generate converters instead of writing them manually.

---

#### Main Flow
- The process starts with `ConverterAnnotationProcessor#process` where it creates `ConverterGenerator` with `ProcessingEnvironment`, `WriterProvider` and list of `Mapper`
- Proceeds to `ConverterGenerator#process` where it loops through all the elements annotated with `@Converter` and proceeds with only those that are Class elements.
  - Loops through all the `@Converter` annotations on that Class element and proceed to `ConverterGenerator#generateCode` for generating converter class for each one of those elements. 
    - Retrieves information for `targetClass` and `sourceClass`, and creates state (`BuilderConverterClassState`) object for managing converter class state.
    - The `state` is then visited by a `ClassStateVisitor` (currently only `BuilderConverterClassStateVisitor`) which processes and updates the `state`, making it ready to be used for filling converter template file.
    - The `state` object is then passed to the `TemplateContentBuilder` where it reads the template file (currently `builder.txt`) and replaces tokens with information from the `state` object, and returns final content to be used for creating source file.
    - Finally, a source file from the content returned from above.

#### Specific Flows
##### BuilderConverterClassStateVisitor
- Starts with `BuilderConverterClassStateVisitor#visit`
- Loops through all class fields from `state` object passed, and proceeds with each field as follows
- Retrieves `fromVar` and `toVar` information, and loops throw `Mapper`s to find mapping `ConversionMappingResult` against them. 
- Updates `state` object using the `ConversionMappingResult` returned

##### Mapper
- Starts with `Mapper#getFor`
- Finds appropriate mapping and returns `ConversionMappingResult`
---
