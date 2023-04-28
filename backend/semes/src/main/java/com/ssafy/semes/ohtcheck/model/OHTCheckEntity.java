package com.ssafy.semes.ohtcheck.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ssafy.semes.oht.model.OHTEntity;
import com.ssafy.semes.wheelcheck.model.WheelCheckEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OHTCheckEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="oht_check_id")
	private Long ohtCheckId;
	@CreatedDate
	@Column(name="oht_check_start_datetime", nullable = false)
	private LocalDateTime ohtCheckStartDatetime;
	@Column(name="oht_check_end_datetime")
	private LocalDateTime ohtCheckEndDatetime;

	@Column(name="good_count_total", nullable = false)
	private int goodCount;
	@Column(name="out_count_total", nullable = false)
	private int outCount;
	@Column(name="lose_count_total", nullable = false)
	private int loseCount;
	@Column(name="unclassified_count_total", nullable = false)
	private int unclassifiedCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "oht_id")
	private OHTEntity oht;

	@OneToMany(mappedBy = "ohtCheck")
	private List<WheelCheckEntity> wheelChecks;

}
