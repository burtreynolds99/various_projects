with Ada.Text_IO; use Ada.Text_IO;
with Ada.Integer_Text_IO; use Ada.Integer_Text_IO;
with Ada.Strings.Unbounded; use Ada.Strings.Unbounded;
with Ada.Strings.Unbounded.Text_IO; use Ada.Strings.Unbounded.Text_IO;
with Ada.Characters.Handling; use Ada.Characters.Handling;

-- Bucky Kittinger
-- Itec 360 Project 2
-- Edit Distance algorithm
procedure EditDist is
   
   -- Simple utility function to take the minimum of three natural numbers
   function Min(one : Natural; two : Natural) return Natural is
   begin
      if (one < two) then
         return one;
      else
         return two;
      end if;
   end Min;

   function Min(one : Natural; two : Natural; three : Natural) return Natural is
   begin
        return Min(Min(one, two), three); 
   end Min;
   
   -- Function to return the edit distance between `searchWord` and `text`
   function EditDistance(searchWord : in String; text : in String) return Natural is
      distMatrix : array(1 .. searchWord'Length+1, 1 .. text'Length+1) of Natural; -- 2d matrix of the edit distances
      inc : Natural;  -- used to determine how much to increment the number by
      A,B : Natural;  -- used to hold the indices for MinDist
   begin
      -- fill in the zeroth row with incrementing numbers
      for I in 1 .. searchWord'Length+1 loop
         distMatrix(I, 1) := I - 1;
      end loop;

      -- fill in the zeroth column with incrementing numbers
      for J in 1 .. text'Length+1 loop
         distMatrix(1, J) := J - 1;
      end loop;

      -- fill in the rest of the matrix based on previous entries
      for I in 1 .. searchWord'Length loop
         A := I + 1;
         for J in 1 .. text'Length loop
            B := J + 1;
            if To_Lower(searchWord(I)) = To_Lower(text(J)) then
               inc := 0;
            else 
               inc := 1;
            end if;
               distMatrix(A, B) := Min(distMatrix(A - 1, B) + 1, distMatrix(A, B - 1) + 1, distMatrix(A - 1, B - 1) + inc);
         end loop;
      end loop;
      -- return the last element in the table
      return distMatrix(searchWord'Length + 1, text'Length + 1);
   end EditDistance;

   -- Procedure to get the next word from the input stream or place 
   -- whitespace and punctuation is ignored
   -- State machine for input
   -- (0) -- (1) on a character that is not whitespace or punctuation (alpha)
   -- (0) -- (2) on a character that is whitespace or punctuation (beta)
   -- (1) -- (1) on alpha
   -- (1) -- (3) on beta
   -- (2) -- (2) on alpha
   -- (3) is the accept state, the machine rolls back at that point
   procedure ProcessInput(searchWord : in String; maxDist : in Natural) is 
      buffer   : String(1 .. 256); -- allow for 256 characters to be read if needed
      buffLen  : Natural;         -- keep an offset to the last character
      index    : Natural;         -- index for the buffer
      c        : Character;
      dist     : Natural;
      EOL      : Boolean;
      state    : Natural;         -- which state are we in
   begin

      state := 0;                 -- start in state 0
      index := 1;                 -- set it to point to the first element
      buffLen := 0;               -- the current length is zero
      c := ' ';
      -- cycle through all of the characters
      while (not End_Of_File) loop
         
         Look_Ahead(c, EOL);
        
         -- check if the next character is the EOL
         if EOL then
            case state is
               when 1 =>
                  state := 3;       -- state three is the accept state
               when others =>
                  state := 2;
                  New_Line;
                  Skip_Line;
            end case;
            goto Continue;
         end if;

         -- check if it is whitespace or punctuation
         if Is_Special(c) then -- skip the punctuation
            case state is
               when 1 =>
                  state := 3;
               when others =>
                  state := 2;
                  Put(c);
            end case;
         else     -- else its a valid character to use to determine if it's within edit distance
            buffer(index) := c;        -- place the character into the buffer
            buffLen := buffLen + 1;    -- increment the buffer length
            index := index + 1;        -- increment the index
            state := 1;
         end if;
         
         Get(c);                       -- consume the character

         <<Continue>>                  -- Dijkstra would hate me, Edit distance considered harmful
         
         if state = 3 then       -- if the state is 1 we are ready to check the edit distance
            dist := EditDistance(searchWord, buffer(1 .. buffLen));
            if (dist <= maxDist) then
               Put("(" & buffer(1..buffLen) & ")");
            else
               Put(buffer(1..buffLen));
            end if;
            Put(c);  -- put the special or whitespace character after the word has been processed

            -- reset the variables
            state := 0;          -- go back to state 1
            index := 1;
            buffLen := 0;
         end if;

      end loop;
   end ProcessInput;
      
   searchWord  : Unbounded_String;  -- used to hold the search word from input
   maxDist     : Natural;           -- used to hold the maximum allowable edit distance
begin
   -- First lets grab the search term
   Get_Line(searchWord);
   Put_Line(searchWord);
   -- then get the maximum edit distance
   Get(maxDist);
   Put(Integer'Image(maxDist));
   ProcessInput(to_string(searchWord), maxDist);
end EditDist;
