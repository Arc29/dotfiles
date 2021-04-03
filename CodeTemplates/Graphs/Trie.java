class Trie {
    
    public class TrieNode {
        
        private TrieNode links[];
        private boolean isEnd;
        private final int ALPHABET_SIZE = 26;
        
        public TrieNode() {
            links = new TrieNode[ALPHABET_SIZE];
        }
        
        public boolean containsKey(char ch) {
            return links[ch - 'a'] != null;
        }
        
        public TrieNode get(char ch) {
            return links[ch - 'a'];
        }
        
        public void put(char ch, TrieNode node) {
            links[ch - 'a'] = node;
        }
        
        public void setEnd() {
            isEnd = true;
        }
        
        public boolean isEnd() {
            return isEnd;
        }
    }

    private TrieNode root;
    
    /** Initialize your data structure here. */
    public Trie() {
        root = new TrieNode();
    }
    
    /** Inserts a word into the trie. */
    public void insert(String word) {
        TrieNode node = root;
        int len = word.length();
        for(int i = 0; i < len; ++i) {
            char current = word.charAt(i);
            if(!node.containsKey(current))
                node.put(current, new TrieNode());
            node  = node.get(current);
        }
        node.setEnd();
    }
    
    /** Returns if the word is in the trie. */
    public boolean search(String word) {
        TrieNode node = searchPrefix(word);
        return node != null && node.isEnd();
    }
    
    public TrieNode searchPrefix(String prefix) {
        TrieNode node = root;
        int len = prefix.length();
        for(int i = 0; i < len; ++i) {
            char current = prefix.charAt(i);
            if(node.containsKey(current))
                node = node.get(current);
            else
                return null;
        }
        return node;
    }
    /** Returns if there is any word in the trie that starts with the given prefix. */
    public boolean startsWith(String prefix) {
        return searchPrefix(prefix) != null;
    }
}

/**
 * Your Trie object will be instantiated and called as such:
 * Trie obj = new Trie();
 * obj.insert(word);
 * boolean param_2 = obj.search(word);
 * boolean param_3 = obj.startsWith(prefix);
 */
