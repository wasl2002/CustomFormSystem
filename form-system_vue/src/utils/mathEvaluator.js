/**
 * 安全的数学表达式计算器
 * 支持四则运算、括号和数字
 */

class MathEvaluator {
  constructor() {
    this.operators = {
      '+': { precedence: 1, associativity: 'left' },
      '-': { precedence: 1, associativity: 'left' },
      '*': { precedence: 2, associativity: 'left' },
      '/': { precedence: 2, associativity: 'left' }
    };
  }

  /**
   * 验证表达式是否只包含允许的字符
   */
  validateExpression(expression) {
    return /^[\d+\-*/().\s]+$/.test(expression);
  }

  /**
   * 将中缀表达式转换为后缀表达式（逆波兰表示法）
   */
  infixToPostfix(expression) {
    const output = [];
    const operators = [];
    
    // 移除空格并分割表达式
    const tokens = this.tokenize(expression);
    
    for (const token of tokens) {
      if (this.isNumber(token)) {
        output.push(parseFloat(token));
      } else if (token === '(') {
        operators.push(token);
      } else if (token === ')') {
        while (operators.length > 0 && operators[operators.length - 1] !== '(') {
          output.push(operators.pop());
        }
        operators.pop(); // 移除 '('
      } else if (this.isOperator(token)) {
        while (
          operators.length > 0 &&
          operators[operators.length - 1] !== '(' &&
          this.hasHigherPrecedence(operators[operators.length - 1], token)
        ) {
          output.push(operators.pop());
        }
        operators.push(token);
      }
    }
    
    while (operators.length > 0) {
      output.push(operators.pop());
    }
    
    return output;
  }

  /**
   * 分割表达式为标记
   */
  tokenize(expression) {
    const tokens = [];
    let currentNumber = '';
    
    for (let i = 0; i < expression.length; i++) {
      const char = expression[i];
      
      if (/\d|\./.test(char)) {
        currentNumber += char;
      } else if (char === '-' && (i === 0 || '()+-*/'.includes(expression[i - 1]))) {
        // 处理负数
        currentNumber += char;
      } else {
        if (currentNumber) {
          tokens.push(currentNumber);
          currentNumber = '';
        }
        if ('()+-*/'.includes(char)) {
          tokens.push(char);
        }
      }
    }
    
    if (currentNumber) {
      tokens.push(currentNumber);
    }
    
    return tokens;
  }

  /**
   * 检查是否为数字
   */
  isNumber(token) {
    return !isNaN(parseFloat(token)) && isFinite(token);
  }

  /**
   * 检查是否为操作符
   */
  isOperator(token) {
    return token in this.operators;
  }

  /**
   * 检查操作符优先级
   */
  hasHigherPrecedence(op1, op2) {
    return (
      this.operators[op1].precedence > this.operators[op2].precedence ||
      (this.operators[op1].precedence === this.operators[op2].precedence &&
        this.operators[op1].associativity === 'left')
    );
  }

  /**
   * 计算后缀表达式
   */
  evaluatePostfix(postfix) {
    const stack = [];
    
    for (const token of postfix) {
      if (typeof token === 'number') {
        stack.push(token);
      } else if (this.isOperator(token)) {
        if (stack.length < 2) {
          throw new Error('表达式格式错误');
        }
        
        const b = stack.pop();
        const a = stack.pop();
        let result;
        
        switch (token) {
          case '+':
            result = a + b;
            break;
          case '-':
            result = a - b;
            break;
          case '*':
            result = a * b;
            break;
          case '/':
            if (b === 0) {
              throw new Error('除数不能为零');
            }
            result = a / b;
            break;
          default:
            throw new Error(`未知操作符: ${token}`);
        }
        
        stack.push(result);
      }
    }
    
    if (stack.length !== 1) {
      throw new Error('表达式格式错误');
    }
    
    return stack[0];
  }

  /**
   * 计算表达式
   */
  evaluate(expression) {
    if (!expression || typeof expression !== 'string') {
      return 0;
    }
    
    // 验证表达式
    if (!this.validateExpression(expression)) {
      throw new Error('表达式包含非法字符');
    }
    
    try {
      // 转换为后缀表达式
      const postfix = this.infixToPostfix(expression);
      
      // 计算结果
      const result = this.evaluatePostfix(postfix);
      
      return isNaN(result) ? 0 : result;
    } catch (error) {
      console.error('计算表达式失败:', error);
      return 0;
    }
  }
}

// 导出单例
export default new MathEvaluator();