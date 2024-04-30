public class Grassland {

     private int[][] meadow;
     private int starveTime;
     private Rabbit[][] rabbits;

     public final static int EMPTY = 0;
     public final static int RABBIT = 1;
     public final static int CARROT = 2;

     public Grassland(int i, int j, int starveTime) {
          // Inicializa a matriz do prado, o tempo de starving e a matriz dos coelhos
          this.meadow = new int[i][j];
          this.starveTime = starveTime;
          this.rabbits = new Rabbit[i][j];

          // Inicializa o prado com células vazias
          for (int x = 0; x < i; x++) {
               for (int y = 0; y < j; y++) {
                    meadow[x][y] = EMPTY;
               }
          }
     }

     public int width() {
          // Retorna a largura do prado, que é o número de colunas na matriz meadow
          return meadow.length;
     }
      
     public int height() {
          // Retorna a altura do prado, que é o número de linhas na matriz meadow
          return meadow[0].length;
     }
      
     public int starveTime() {
          // Retorna o tempo de starving configurado para os coelhos
          return starveTime;
     }

     public void addCarrot(int x, int y) {
          // Verifica se a célula está vazia
          if (meadow[x][y] == EMPTY) {
               // Coloca uma cenoura na célula se estiver vazia
               meadow[x][y] = CARROT;
          }
     }

     public void addRabbit(int x, int y, Rabbit rabbit) {
          // Verifica se a célula está vazia
          if (meadow[x][y] == EMPTY) {
               // Coloca um coelho recém-nascido na célula se estiver vazia
               meadow[x][y] = RABBIT;
               rabbits[x][y] = rabbit;
          }
     }     

     public int cellContents(int x, int y) {
          // Retorna o conteúdo da célula na posição (x, y) no prado
          return meadow[x][y];
     }

     public void copyRabbits(Grassland source) {
          // Copia a matriz de coelhos da origem para o destino
          for (int x = 0; x < width(); x++) {
              for (int y = 0; y < height(); y++) {
                  this.rabbits[x][y] = source.rabbits[x][y];
              }
          }
     }

     public Grassland timeStep() {
          // Cria um novo prado
          Grassland newMeadow = new Grassland(width(), height(), starveTime);
      
          // Percorre cada célula no prado atual
          for (int x = 0; x < width(); x++) {
               for (int y = 0; y < height(); y++) {
                    // Aplica as regras conforme o conteúdo atual da célula
                    int newContent = determineNextContent(x, y);
      
                    // Atualiza o novo prado com o conteúdo determinado
                    newMeadow.meadow[x][y] = newContent;
               }
          }

          newMeadow.copyRabbits(this);
      
          return newMeadow;
     }

     public static class Rabbit {
          private int rabbitStarveTime;
      
          // Construtor da classe Rabbit, inicializa o starveTime do coelho a 0
          public Rabbit() {
               rabbitStarveTime = 0;
          }
      
          // Obtém o valor atual do starveTime do coelho
          public int starveTime() {
               return rabbitStarveTime;
          }
      
          // Reinicia o starveTime
          public void eatCarrot() {
               rabbitStarveTime = 0;
          }
      
          // Aumenta o starveTime quando o coelho não come
          public void increaseStarveTime() {
               rabbitStarveTime++;
          }
     }
      

     public void printMatrix() {
          System.out.println("Prado:"); 
          for (int y = 0; y < height(); y++) {
               for (int x = 0; x < width(); x++) {
                    if (rabbits[x][y] != null) {
                         System.out.print("R ");  // Se há um coelho na célula, escreve "R"
                    } else {
                         switch (meadow[x][y]) {
                              case CARROT:
                                   System.out.print("C ");  // Se há uma cenoura na célula, escreve "C"
                                   break;
                              case EMPTY:
                                   System.out.print("E ");  // Se a célula está vazia, escreve "E"
                                   break;
                         }
                    }
               }
               System.out.println();  // Nova linha para cada linha da matriz
          }
     }
      

     private int determineNextContent(int x, int y) {
          int currentContent = meadow[x][y];  // Obtém o conteúdo atual da célula
          int carrotNeighbors = countCarrotNeighbors(x, y);  // Conta o número de vizinhos com cenouras
          int rabbitNeighbors = countRabbitNeighbors(x, y);  // Conta o número de vizinhos com coelhos
      
          int newContent = currentContent;  // Inicializa com o valor atual da célula
      
          // Aplica as regras conforme o conteúdo atual da célula
          switch (currentContent) {
               case EMPTY:
                    if (carrotNeighbors < 2) {
                         newContent = EMPTY;  // Célula vazia permanece vazia se tiver menos de 2 vizinhos com cenouras
                    } else if (carrotNeighbors >= 2 && rabbitNeighbors <= 1) {
                         newContent = CARROT;  // Célula vazia vira cenoura se tiver 2 ou mais vizinhos com cenouras e no máximo 1 vizinho com coelho
                    } else if (carrotNeighbors >= 2 && rabbitNeighbors >= 2) {
                         newContent = RABBIT;  // Célula vazia vira coelho se tiver 2 ou mais vizinhos com cenouras e 2 ou mais vizinhos com coelhos
                         rabbits[x][y] = new Rabbit();  // Adiciona um novo coelho à posição
                    }
                  break;
      
               case RABBIT:
                    if (carrotNeighbors != 0) {
                         newContent = RABBIT;
                         rabbits[x][y].eatCarrot();    // Coelho comeu uma cenoura, reinicia o starveTime
                    } else {
                         // Verifica se o coelho excedeu o starveTime
                         if (rabbits[x][y].starveTime() >= starveTime) {
                              newContent = EMPTY;  // Remove o coelho se exceder o starveTime
                              rabbits[x][y] = null;
                         } else {
                              // Aumenta o starveTime quando o coelho não come
                              rabbits[x][y].increaseStarveTime();
                              newContent = RABBIT;
                         }
                    }
                    break;
      
               case CARROT:
                    if (rabbitNeighbors == 0) {
                         newContent = CARROT;  // Cenoura permanece se não houver vizinhos com coelhos
                    } else if (rabbitNeighbors >= 2) {
                         newContent = RABBIT;  // Cenoura vira coelho se tiver 2 ou mais vizinhos com coelhos
                         rabbits[x][y] = new Rabbit();  // Adiciona um novo coelho à posição
                    } else {
                         newContent = EMPTY;  // Cenoura é removida se tiver 1 vizinho com coelho
                    }
                    break;
          }
      
          return newContent;  // Retorna o novo conteúdo da célula
     }
      

     private int countCarrotNeighbors(int x, int y) {
          int count = 0;  // Inicia a contagem de vizinhos com cenouras
     
          // Loop para percorrer as células vizinhas
          for (int dx = -1; dx <= 1; dx++) {
               for (int dy = -1; dy <= 1; dy++) {
                    if (dx != 0 || dy != 0) {  // Garante que não estamos na célula central
                         // Calcula as coordenadas da célula vizinha
                         int newX = (x + dx + width()) % width();
                         int newY = (y + dy + height()) % height();
      
                         // Verifica se a célula vizinha contém uma cenoura e incrementa a contagem
                         if (meadow[newX][newY] == CARROT) {
                              count++;
                         }
                    }
               }
          }
      
          return count;  // Retorna o número de vizinhos com cenouras
     }

     private int countRabbitNeighbors(int x, int y) {
          int count = 0;      // Inicia a contagem de vizinhos com coelhos

          // Loop para percorrer as células vizinhas
          for (int dx = -1; dx <= 1; dx++) {
               for (int dy = -1; dy <= 1; dy++) {
                    if (dx != 0 || dy != 0) {// Garante que não estamos na célula central
                         // Calcula as coordenadas da célula vizinha
                         int newX = (x + dx + width()) % width();
                         int newY = (y + dy + height()) % height();

                         // Verifica se a célula vizinha contém um coelho e incrementa a contagem
                         if (meadow[newX][newY] == RABBIT) {
                              count++;
                         }
                    }
               }
          }

          return count;
     }

}
