echo "install Vim pathogen"
mkdir -p ~/.vim/autoload ~/.vim/bundle && \
    curl -LSso ~/.vim/autoload/pathogen.vim https://tpo.pe/pathogen.vim

echo "Vim plugins"
git clone ~/.vim/bundle/https://github.com/derekwyatt/vim-scala
git clone ~/.vim/bundle/https://github.com/kien/ctrlp.vim
git clone ~/.vim/bundle/https://github.com/tpope/vim-commentary

echo "Configure Vim"
cat << EOF > ~/.vimrc
execute pathogen#infect()
syntax on
filetype plugin indent on
set shortmess+=I
set wildignore+=*/target/*
EOF

exit 0
