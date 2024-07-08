# IHAutoClicker
Auto clicker criado para fins experimentais e de aprendizado

### Dependências
+ evdev library `Linux`

### Tecnologias
- Java: Usado como interface gráfica principal atrávez do JavaFX, e o tambem foi utilizado o JNI para a utilização das livrarias nativas para o funcionamento de inputs do mosue
- C/C++: Usado para a criação das livrarias nativas

### Portabilidade
- [x] Linux (Debian)
- [x] Windows (11)
- [ ] Darwin

### Desafios
- Input do mouse nativamente: Na primeiva versão não publicada do projeto, eu utilizei a versão 8, onde existia uma implementação do JavaxSwing (Robot) que já fazia o trabalho de inserir, a nova interface JavaFX também possui, porém, com muitas limitações, o que tornava o projeto impossível de ser feito, então tive que criar as libs nativas.
- JavaFX: Como não conhecia muito bem essa API, me deu um pouco de dor de cabeça, justo que meu objetivo era que fosse um arquivo jar (Assim como foi na primeira versão não publicada do projeto)