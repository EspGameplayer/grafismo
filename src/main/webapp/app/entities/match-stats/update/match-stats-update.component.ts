import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMatchStats, MatchStats } from '../match-stats.model';
import { MatchStatsService } from '../service/match-stats.service';
import { IMatch } from 'app/entities/match/match.model';
import { MatchService } from 'app/entities/match/service/match.service';

@Component({
  selector: 'jhi-match-stats-update',
  templateUrl: './match-stats-update.component.html',
})
export class MatchStatsUpdateComponent implements OnInit {
  isSaving = false;

  matchesCollection: IMatch[] = [];

  editForm = this.fb.group({
    id: [],
    homePossessionTime: [null, [Validators.min(0)]],
    awayPossessionTime: [null, [Validators.min(0)]],
    inContestPossessionTime: [null, [Validators.min(0)]],
    match: [null, Validators.required],
  });

  constructor(
    protected matchStatsService: MatchStatsService,
    protected matchService: MatchService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ matchStats }) => {
      this.updateForm(matchStats);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const matchStats = this.createFromForm();
    if (matchStats.id !== undefined) {
      this.subscribeToSaveResponse(this.matchStatsService.update(matchStats));
    } else {
      this.subscribeToSaveResponse(this.matchStatsService.create(matchStats));
    }
  }

  trackMatchById(_index: number, item: IMatch): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatchStats>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(matchStats: IMatchStats): void {
    this.editForm.patchValue({
      id: matchStats.id,
      homePossessionTime: matchStats.homePossessionTime,
      awayPossessionTime: matchStats.awayPossessionTime,
      inContestPossessionTime: matchStats.inContestPossessionTime,
      match: matchStats.match,
    });

    this.matchesCollection = this.matchService.addMatchToCollectionIfMissing(this.matchesCollection, matchStats.match);
  }

  protected loadRelationshipsOptions(): void {
    this.matchService
      .query({ filter: 'matchstats-is-null' })
      .pipe(map((res: HttpResponse<IMatch[]>) => res.body ?? []))
      .pipe(map((matches: IMatch[]) => this.matchService.addMatchToCollectionIfMissing(matches, this.editForm.get('match')!.value)))
      .subscribe((matches: IMatch[]) => (this.matchesCollection = matches));
  }

  protected createFromForm(): IMatchStats {
    return {
      ...new MatchStats(),
      id: this.editForm.get(['id'])!.value,
      homePossessionTime: this.editForm.get(['homePossessionTime'])!.value,
      awayPossessionTime: this.editForm.get(['awayPossessionTime'])!.value,
      inContestPossessionTime: this.editForm.get(['inContestPossessionTime'])!.value,
      match: this.editForm.get(['match'])!.value,
    };
  }
}
