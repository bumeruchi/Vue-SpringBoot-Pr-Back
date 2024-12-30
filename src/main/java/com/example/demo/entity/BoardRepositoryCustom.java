package com.example.demo.entity;

import com.example.demo.model.SearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.demo.entity.QBoardEntity.boardEntity;

@RequiredArgsConstructor
@Repository
public class BoardRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public Page<BoardEntity> findAllBySearchCondition(Pageable pageable, SearchCondition searchCondition) {
        JPAQuery<BoardEntity> query = queryFactory.selectFrom(boardEntity).where(searchKeywords(searchCondition.getSk(), searchCondition.getSv()));

        long total = query.stream().count();

        List<BoardEntity> results = query
                .where(searchKeywords(searchCondition.getSk(), searchCondition.getSv()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(boardEntity.idx.desc())
                .fetch();
        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression searchKeywords(String sk, String sv) {
        if ("author".equals(sk)) {
            if (StringUtils.hasLength(sv)) {
                return boardEntity.author.contains(sv);
            }
        } else if ("title".equals(sk)) {
            if (StringUtils.hasLength(sv)) {
                return boardEntity.title.contains(sv);
            }
        } else if ("contents".equals(sk)) {
            if (StringUtils.hasLength(sv)) {
                return boardEntity.contents.contains(sv);
            }
        }

        return null;
    }
}
